package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.QztBusiness;
import com.qzt.bus.model.QztGorder;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.IQztBusinessService;
import com.qzt.bus.rpc.api.IQztGorderService;
import com.qzt.bus.rpc.api.IQztUserService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.mq.pushMq.MqConstant;
import com.qzt.common.mq.pushMq.MqMess;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztServiceOrder;
import com.qzt.bus.rpc.api.IQztServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@RestController
@Api(value = "WebQztServiceOrderController", description = "WebQztServiceOrderController")
public class WebQztServiceOrderController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztServiceOrderService service;
    @Autowired
    private IQztGorderService iQztGorderService;
    @Autowired
    private IQztBusinessService iQztBusinessService;
    @Autowired
    private IQztUserService iQztUserService;

    /**
     * 分页查询
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bId", value = "商户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "state", value = "状态（01待确认、03待服务、05已到店、07已完成、90已驳回）", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztServiceOrder/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize,Long bId,String state,Long userId) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            conditionMap.put("busniessId",bId);
            conditionMap.put("userId",userId);
            conditionMap.put("orderState",state);
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.service.find(pageModel);
            List<QztServiceOrder> records = pageModel.getRecords();
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            for(QztServiceOrder qztServiceOrder:records){
                Map<String,Object> map1 = new HashMap<String,Object>();
                QztGorder qztGorder = this.iQztGorderService.queryByOrederNo(qztServiceOrder.getOrderNo());
                /*QztBusiness qztBusiness = this.iQztBusinessService.queryById(qztServiceOrder.getBusniessId());*/
               /* if(qztGorder==null || qztBusiness==null){
                    continue;
                }*/
                map1.put("id",qztServiceOrder.getId());
                map1.put("orderNo",qztServiceOrder.getOrderNo());
                map1.put("createTime",qztServiceOrder.getCreateTime());
                map1.put("url", qztGorder.getGoodsPic());
                map1.put("name", qztGorder.getGoodsName());
                map1.put("server", qztServiceOrder.getBusniessName());
                map1.put("cName", qztServiceOrder.getContactsName());
                map1.put("cTel", qztServiceOrder.getContactsTel());
                map1.put("disease", qztServiceOrder.getDisease());
                list.add(map1);
            }
            map.put("current", pageModel.getCurrent());
            map.put("size", pageModel.getSize());
            map.put("total", pageModel.getTotal());
            map.put("data", list);
            Map backData = new HashMap();
            backData.put("pageData", map);
            return returnUtil.returnMessMap(backData);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "state", value = "状态（01待确认、03待服务、05已到店、07已完成、90已驳回）", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztServiceOrder/getBusListPage")
    public Map<String, Object> getBusListPage(String vno, String cno, Integer pageNum, Integer pageSize,String state,Long userId) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            QztBusiness qztBusiness = this.iQztBusinessService.queryByUserId(userId);
            conditionMap.put("busniessId",qztBusiness.getId());
            conditionMap.put("orderState",state);
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.service.find(pageModel);
            List<QztServiceOrder> records = pageModel.getRecords();
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            for(QztServiceOrder qztServiceOrder:records){
                Map<String,Object> map1 = new HashMap<String,Object>();
                QztGorder qztGorder = this.iQztGorderService.queryByOrederNo(qztServiceOrder.getOrderNo());
                /*QztBusiness qztBusiness = this.iQztBusinessService.queryById(qztServiceOrder.getBusniessId());
                if(qztGorder==null || qztBusiness==null){
                    continue;
                }*/
                map1.put("id",qztServiceOrder.getId());
                map1.put("orderNo",qztServiceOrder.getOrderNo());
                map1.put("createTime",qztServiceOrder.getCreateTime());
                map1.put("url", qztGorder.getGoodsPic());
                map1.put("name", qztGorder.getGoodsName());
                map1.put("server", qztServiceOrder.getBusniessName());
                map1.put("cName", qztServiceOrder.getContactsName());
                map1.put("cTel", qztServiceOrder.getContactsTel());
                map1.put("disease", qztServiceOrder.getDisease());
                list.add(map1);
            }
            map.put("current", pageModel.getCurrent());
            map.put("size", pageModel.getSize());
            map.put("total", pageModel.getTotal());
            map.put("data", list);
            Map backData = new HashMap();
            backData.put("pageData", map);
            return returnUtil.returnMessMap(backData);
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
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztServiceOrder/selectById/{id}")
    public Map<String, Object> selectById(@PathVariable Long id) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            QztServiceOrder qztServiceOrder = this.service.selectById(id);
            QztGorder qztGorder = this.iQztGorderService.queryByOrederNo(qztServiceOrder.getOrderNo());
            QztBusiness qztBusiness = this.iQztBusinessService.queryById(qztServiceOrder.getBusniessId());
            if(qztGorder==null || qztBusiness==null){
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }

//            map.put("orderNo",qztServiceOrder.getOrderNo());
//            map.put("createTime",qztServiceOrder.getCreateTime());
//
//            map.put("url", qztGorder.getGoodsPic());
//            map.put("name", qztGorder.getGoodsName());
//
//            map.put("server", qztBusiness.getBusName());
            map.put("status", qztServiceOrder.getOrderState());//状态
            map.put("service", qztGorder.getGoodsName());//服务项目
            map.put("name", qztServiceOrder.getContactsName());//申请人
            map.put("tel", qztServiceOrder.getContactsTel());//联系电话
            map.put("station", qztBusiness.getBusName());//所属服务站
            map.put("beginTime", qztServiceOrder.getCreateTime());//申请服务时间
            map.put("endTime", qztServiceOrder.getFinishTime());//订单完成时间
            map.put("disease", qztServiceOrder.getDisease());
            return returnUtil.returnMess(map);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "新增服务订单", notes = "新增服务订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "联系人", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tel", value = "联系电话", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "oId", value = "订单编号（注意不是订单ID）", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "dis", value = "疾病名称", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bId", value = "商户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztServiceOrder/add")
    public Map<String, Object> add(String tokenId,Long userId,String name,String tel,String oId,String dis,Long bId) {
        try {
            QztGorder qztGorder = this.iQztGorderService.queryByOrederNo(oId);
            QztBusiness qztBusiness = this.iQztBusinessService.queryById(bId);
            if(qztGorder==null || qztBusiness==null){
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
            if(!qztGorder.getOrderState().equals("09") || !qztGorder.getIsServe().equals("Y")){
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
            if(!qztBusiness.getBusState().equals("0")){
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
            QztServiceOrder qztServiceOrder = new QztServiceOrder();
            qztServiceOrder.setBusniessId(bId);
            qztServiceOrder.setContactsName(name);
            qztServiceOrder.setContactsTel(tel);
            qztServiceOrder.setUserId(userId);
            qztServiceOrder.setOrderNo(oId);
            qztServiceOrder.setOrderState("01");
            qztServiceOrder.setShareMoney(qztGorder.getShareMoney());
            qztServiceOrder.setDisease(dis);
            this.service.add(qztServiceOrder);
            this.iQztGorderService.updateIsServe(oId,"N",userId);//修改订单状态
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "订单状态变更", notes = "订单状态变更")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "sId", value = "服务ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "是否确认（0是1否）", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztServiceOrder/update")
    public Map<String, Object> update(String tokenId,Long userId,Long sId,String type) {
        try {
            QztServiceOrder qztServiceOrder = this.service.queryById(sId);
            if(qztServiceOrder==null||type==null||qztServiceOrder.getOrderState()==null||qztServiceOrder.getOrderState().equals("90")||qztServiceOrder.getOrderState().equals("96")){
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
            if(type.equals("0")){
                switch (qztServiceOrder.getOrderState()) {//（01待确认、03待服务、05已到店、07已完成、90已驳回、96异常订单）
                    case "01":
                        qztServiceOrder.setOrderState("03");
                        qztServiceOrder.setFinishTime(new Date());
                        this.service.modifyById(qztServiceOrder);
                        break;
                    case "03":
                        qztServiceOrder.setOrderState("05");
                        qztServiceOrder.setFinishTime(new Date());
                        this.service.modifyById(qztServiceOrder);
                        break;
                    case "05":
                        qztServiceOrder.setOrderState("07");
                        qztServiceOrder.setFinishTime(new Date());
                        this.service.modifyById(qztServiceOrder);
                        new MqMess(MqConstant.SERVICES_MONEY_TOP,qztServiceOrder.getId().toString()).sendTop();
                        break;
                    default:
                        returnUtil.returnMess(Constant.DATA_ERROR);
                        break;
                }

            }else if(type.equals("1")){
                qztServiceOrder.setOrderState("90");
                qztServiceOrder.setFinishTime(new Date());
                this.service.modifyById(qztServiceOrder);
                this.iQztGorderService.updateIsServe(qztServiceOrder.getOrderNo(),"Y",userId);//修改订单状态
            }

            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "订单状态变更", notes = "订单状态变更")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "oId", value = "订单编号", dataType = "String", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztServiceOrder/check")
    public Map<String, Object> check(String tokenId,Long userId,String oId) {
//        try {
//            Map<String, Object> map = new HashMap<String, Object>();
//            QztGorder qztGorder = this.iQztGorderService.queryByOrederNo(oId);
//            if(qztGorder==null){
                return returnUtil.returnMess(Constant.DATA_ERROR);
//            }
//            if(qztGorder.getShareCode()!=null && !qztGorder.getShareCode().equals("")){
//                QztUser qztUser = this.iQztUserService.findUserById(qztGorder.getShareCode());
//                if(qztUser!=null && qztUser.getUserType()==10) {
//                    map.put("code","200");
//                    QztBusiness business = this.iQztBusinessService.queryByUserId(qztUser.getUserId());
//                    map.put("busName",business.getBusName());
//                    map.put("orderNo",business.getId());
//                    return map;
//                }else {
//                    map.put("code","201");
//                    return map;
//                }
//            }else {
//                map.put("code","201");
//                return map;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
    }
}
