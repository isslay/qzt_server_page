package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.DgmCouponIntegral;
import com.qzt.bus.rpc.api.IDgmCouponIntegralService;
import com.qzt.bus.rpc.api.IDgmIntegralRecordService;
import com.qzt.bus.rpc.api.IDgmUsableIntegralService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author snow
 * @since 2023-09-06
 */
@RestController
@Api(value = "WebDgmCouponIntegralController", description = "WebDgmCouponIntegralController")
public class WebDgmCouponIntegralController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IDgmCouponIntegralService service;

    @Autowired
    private IDgmIntegralRecordService iDgmIntegralRecordService;

    @Autowired
    private IDgmUsableIntegralService iDgmUsableIntegralService;

    /**
     * 分页查询
     *
     * @return Map
     * @author snow
     * @date 2023-09-06
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/dgmCouponIntegral/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.service.find(pageModel);
            List<DgmCouponIntegral> records = pageModel.getRecords();
            map.put("current", pageModel.getCurrent());
            map.put("size", pageModel.getSize());
            map.put("total", pageModel.getTotal());
            map.put("data", records);
            return returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 根据主键id查询详情
     *
     * @param id
     * @return Map
     * @author snow
     * @date 2023-09-06
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/dgmCouponIntegral/selectById")
    public Map<String, Object> selectById(String vno, String cno, Long id) {
        try {
            DgmCouponIntegral entity = this.service.selectById(id);
            return returnUtil.returnMess(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "用户兑换积分优惠券", notes = "用户兑换积分优惠券")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "couponId", value = "券ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "num", value = "兑换数量", dataType = "int", required = true, paramType = "query"),
    })
    @PostMapping("/webapi/dgmCouponIntegral/exchange")
    public Map<String, Object> exchange(String vno, String cno, String tokenId, Long userId, Long couponId, int num) {
        try {
            DgmCouponIntegral entity = this.service.selectById(couponId);
            if (entity == null || entity.getStatus() == 1 || num <= 0) {
                return returnUtil.returnMess("201", "优惠券不可用或已下架！");
            }
            int value = entity.getNumber() * num;
            if (value > 0) {
                int i = iDgmIntegralRecordService.consumeNew(userId.intValue(), 21, "积分兑换", value);
                if (i == 1) {
                    return returnUtil.returnMess("201", "积分不足！");
                }
                service.creatCouponIntegral(userId, couponId, num);
            }
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 根据用户id查询积分
     *
     * @param
     * @return Map
     * @author snow
     * @date 2023-09-06
     */
    @ApiOperation(value = "根据用户id查询积分", notes = "根据用户id查询积分")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query")
    })
    @GetMapping("/webapi/dgmCouponIntegral/integralById")
    public Map<String, Object> integralById(String vno, String cno, String tokenId, Long userId) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("integral", iDgmUsableIntegralService.sumAll(userId.intValue()));
            map.put("monthOver", iDgmUsableIntegralService.monthOver(userId.intValue()));
            return returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}

