package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.QztGorder;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.IQztGorderService;
import com.qzt.bus.rpc.api.IQztUserService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.DgmOrderRefund;
import com.qzt.bus.rpc.api.IDgmOrderRefundService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单--退款处理_web前端控制器
 * </p>
 *
 * @author snow
 * @since 2023-11-06
 */
@RestController
@Api(value = "WebDgmOrderRefundController", description = "WebDgmOrderRefundController")
public class WebDgmOrderRefundController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IDgmOrderRefundService service;

    @Autowired
    private IQztGorderService qztGorderService;

    @Autowired
    private IQztUserService userService;

    /**
     * 取消订单
     *
     * @return Map
     * @author snow
     * @date 2023-11-06
     */
    @ApiOperation(value = "取消订单", notes = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query")
    })
    @PostMapping("/webapi/dgmOrderRefund/submit")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize, String orderNo, Long userId) {
        try {
            if (orderNo != null && userId != null) {

                QztGorder qztGorder = this.qztGorderService.queryByOrederNoUserId(new QztGorder(userId, orderNo));

                if (qztGorder == null) {
                    return returnUtil.returnMess("201", "未查询到相关订单");
                }

                QztUser qztUser = userService.findDGUserById(userId);
                if(qztUser == null){
                    return returnUtil.returnMess("201", "未查询到相关用户");
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date curDate = new Date();
                String ss = sdf.format(curDate);
                String orderDate = sdf.format(qztGorder.getCreateTime());
                if(!ss.equals(orderDate)){
                    return returnUtil.returnMess("201", "订单超出日期，不可退款");
                }

                int i = this.qztGorderService.cancellationOfOrder1(new QztGorder(userId, orderNo));
                if (i == 0) {
                    return returnUtil.returnMess("201", "订单状态已变更，申请失败");
                }

                Map map = new HashMap();
                String reason = "消费者申请退费";
                String orderId = qztGorder.getOrderNo();
                String refundId = "TF" + qztGorder.getOrderNo();
                int totalPrice = Math.toIntExact(qztGorder.getActuaPayment());

                DgmOrderRefund dgmOrderRefund = new DgmOrderRefund();
                dgmOrderRefund.setOrderId(orderId);
                dgmOrderRefund.setOutRefundId(refundId);
                dgmOrderRefund.setUserId(Math.toIntExact(userId));
                dgmOrderRefund.setUserName(qztUser.getRealName());
                dgmOrderRefund.setRefundReason(reason);
                dgmOrderRefund.setPayMoney(totalPrice);

                this.service.insert(dgmOrderRefund);

                map.put("orderId", orderId);
                map.put("refundId", refundId);
                map.put("reason", reason);
                map.put("totalPrice", totalPrice);

                return returnUtil.returnMessMap(map);
            } else {
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}

