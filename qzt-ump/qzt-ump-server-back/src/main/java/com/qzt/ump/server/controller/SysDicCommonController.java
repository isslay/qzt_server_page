package com.qzt.ump.server.controller;


import com.qzt.common.core.constant.Constant;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>字典公用前端用户管理 前端控制器
 * </p>
 *
 * @author cgw
 * @since 2018-05-25
 */
@RestController
@Slf4j
@Api(value = "字典公用" , description = "字典公用接口" )
public class SysDicCommonController extends BaseController {

    @Autowired
    private ReturnUtilServer returnUtil;

    /**
     * 查询日志分页方法
     *
     * @param dicType
     * @return ResultModel
     * @author cgw
     * @date 18/05/25 12:28:13
     */

    @ApiOperation(value = "字典公用" , notes = "字典公用" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dicType" , value = "字典类别" , dataType = "String" , paramType = "query" , required = true),
    })
    @PostMapping("webapi/sysDic" )
    public Map<String, Object> sysDic(String dicType) {
        try {
            if (dicType == null || "".equals(dicType)) {
                return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
            }
            String[] dicTypeAr = dicType.split("," );
            Map<String, Object> dicData = new HashMap<String, Object>();
            for (String dicObj : dicTypeAr) {
                List<Map> dicMap = DicParamUtil.getDicList(dicObj);
                if (!dicMap.isEmpty()) {
                    dicData.put(dicObj, dicMap);
                }
            }
            return returnUtil.returnMessMap(dicData);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }
}

