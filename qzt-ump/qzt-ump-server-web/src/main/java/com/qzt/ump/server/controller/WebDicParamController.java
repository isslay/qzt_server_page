package com.qzt.ump.server.controller;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.ump.model.SysParamModel;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Api(value = "WebDicParamController", description = "App字典、参数公用接口")
public class WebDicParamController {

    @Autowired
    private ReturnUtilServer returnUtil;

    /**
     * App字典公用
     *
     * @param dicType
     * @return ResultModel
     * @author Xiaofei
     * @date 2018/08/24 09:21:00
     */
    @ApiOperation(value = "App字典公用", notes = "字典类别，查多个英文逗号‘,’分隔")
    @ApiImplicitParam(name = "dicType", value = "字典类别", dataType = "String", paramType = "query", required = true)
    @PostMapping("/pubapi/app/selectDic")
    public Map<String, Object> selectDic(String dicType) {
        try {
            String[] dicTypeAr = dicType.split(",");
            Map<String, Object> dicData = new HashMap<String, Object>();
            for (String dicObj : dicTypeAr) {
                List<Map> dicMap = DicParamUtil.getDicList(dicObj);
                if (!dicMap.isEmpty()) {
                    dicData.put(dicObj, dicMap);
                }
            }
            return returnUtil.returnMessMap(dicData);
        }catch (Exception e){
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * App参数公用
     *
     * @param paramKeys
     * @return ResultModel
     * @author Xiaofei
     * @date 2018/08/24 09:21:00
     */
    @ApiOperation(value = "App参数公用", notes = "App参数公用")
    @ApiImplicitParam(name = "paramKeys", value = "key", dataType = "String", paramType = "query", required = true)
    @PostMapping("/pubapi/app/selectParam")
    public Map<String, Object> selectParam(String paramKeys) {
        try {
            String[] paramKey = paramKeys.split(",");
            Map<String, Object> paramMap = new HashMap<String, Object>();
            for (String paramK : paramKey) {
                //支付乐购币手续费比例
                BigDecimal tescocurrency = new BigDecimal(0);
                SysParamModel sysParamModel = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value() + paramK);
                if (sysParamModel != null) {
                    tescocurrency = new BigDecimal(sysParamModel.getParamValue());
                }
                paramMap.put(paramK, tescocurrency);
            }
            return returnUtil.returnMessMap(paramMap);
        }catch (Exception e){
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


}
