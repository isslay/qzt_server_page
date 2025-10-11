package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.QztBusPic;
import com.qzt.bus.model.QztBusiness;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.*;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.OrderUtil;
import com.qzt.common.tools.StringUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztApplyTryorder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@RestController
@Log4j
@Api(value = "WebQztApplyTryorderController", description = "WebQztApplyTryorderController")
public class WebQztApplyTryorderController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztApplyTryorderService service;

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IQztBusinessService qztBusinessService;

    @Autowired
    private IQztBusLogService qztBusLogService;

    @Autowired
    private IQztBusPicService qztBusPicService;


    /**
     * 分页查询
     *
     * @return Map
     * @author Cgw
     * @date 2019-11-11
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(H5、IOS、Android、PC)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyTryorder/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize, String tokenId, Long userId, String type) {
        try {
            Map returnMap = new HashMap();
            Map conditionMap = new HashMap();
            PageModel pageModel = new PageModel();
            pageModel.setCurrent(pageNum);
            pageModel.setSize(pageSize);
            conditionMap.put("userId", userId);
            pageModel.setCondition(conditionMap);
            pageModel = (PageModel) service.find(pageModel);
            returnMap.put("current", pageModel.getCurrent());
            returnMap.put("size", pageModel.getSize());
            returnMap.put("total", pageModel.getTotal());
            List<QztApplyTryorder> records = pageModel.getRecords();
            List rsData = new ArrayList();
            for (QztApplyTryorder qztApplyTryorder : records) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("obj", qztApplyTryorder);
                dataMap.put("stateMc", DicParamUtil.getDicCodeByType("TRY_STATE", qztApplyTryorder.getOrderState()));
                rsData.add(dataMap);
            }
            returnMap.put("data", rsData);
            Map backData = new HashMap();
            backData.put("pageData", returnMap);
            return returnUtil.returnMessMap(backData);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单查询错误信息：" + e.getMessage());
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "服务站服务订单分页查询", notes = "服务站服务订单分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(H5、IOS、Android、PC)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "state", value = "状态", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyTryorder/getServiceListPage")
    public Map<String, Object> getServiceListPage(String vno, String cno, Integer pageNum, Integer pageSize, String tokenId, Long userId, String state, String orderNo) {
        try {
            Map returnMap = new HashMap();
            Map conditionMap = new HashMap();
            PageModel pageModel = new PageModel();
            pageModel.setCurrent(pageNum);
            pageModel.setSize(pageSize);
            conditionMap.put("busId", userId);
            conditionMap.put("orderNo", orderNo);
            conditionMap.put("orderState", state);
            pageModel.setCondition(conditionMap);
            pageModel = (PageModel) service.find(pageModel);
            returnMap.put("current", pageModel.getCurrent());
            returnMap.put("size", pageModel.getSize());
            returnMap.put("total", pageModel.getTotal());
            returnMap.put("data", pageModel.getRecords());
            Map backData = new HashMap();
            backData.put("pageData", returnMap);
            return returnUtil.returnMessMap(backData);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单查询错误信息：" + e.getMessage());
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 根据主键id查询详情
     *
     * @param id
     * @return Map
     * @author Cgw
     * @date 2019-11-11
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztApplyTryorder/selectById/{id}")
    public Map<String, Object> selectById(@PathVariable Long id) {
        try {
            QztApplyTryorder entity = this.service.selectById(id);
            return returnUtil.returnMess(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "申请试用订单获得基本信息", notes = "申请试用订单获得基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "前台code", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "shareCode", value = "推荐码", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyTryorder/applyOrderIndex")
    public Map<String, Object> applyOrderIndex(String shareCode, String tokenId, String userId) {
        try {
            Map dataMap = new HashMap();
            List<Map> dicMap = DicParamUtil.getDicList("APPLY_TYPE");
            dataMap.put("applyType", dicMap);
            //查询分享的人是否是服务站
            Map params = new HashMap();
//            if (!StringUtil.isEmpty(shareCode)) {
//                QztUser qztUser = qztUserService.findUserById(shareCode);
//                if (qztUser != null && qztUser.getUserType() > 0) {
//                    params.put("userId", qztUser.getUserId());
//                }
//            }
            //查询所有服务站
            params.put("busState", "0");
            List<QztBusiness> qztBusinesses = qztBusinessService.findAllBussiness(params);
            if (qztBusinesses.size() == 0) {
                params.remove("userId");
                qztBusinesses = qztBusinessService.findAllBussiness(params);
            }
            dataMap.put("bussinessData", qztBusinesses);
            return returnUtil.returnMess(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "申请试用订单获得基本信息", notes = "申请试用订单获得基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "前台code", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "storeName", value = "联系姓名", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "storeTel", value = "联系方式", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "diseaseType", value = "疾病类型", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "diseaseName", value = "疾病名称", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "busId", value = "商家ID", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "busName", value = "商家姓名", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "referrerUserId", value = "推荐人ID", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyTryorder/applyOrderCheck")
    public Map<String, Object> applyOrderCheck(String storeName, String storeTel, String diseaseType, String diseaseName, String busId, String busName, Long referrerUserId, String tokenId, String userId) {
        try {
            if (StringUtil.isEmpty(storeName) || StringUtil.isEmpty(storeTel) || StringUtil.isEmpty(diseaseType) || StringUtil.isEmpty(diseaseName) || StringUtil.isEmpty(busId) || StringUtil.isEmpty(busName)) {
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }

            //检测用户手机号和病类型是否已经下单
            Map<String, Object> params = new HashMap<>();
            params.put("storeName", storeTel);
            params.put("diseaseType", diseaseType);
            if (!this.service.checkTryOrder(params)) {
                return returnUtil.returnMess(Constant.HAVE_TRY);
            }

            QztApplyTryorder qztApplyTryorder = new QztApplyTryorder();
            qztApplyTryorder.setStoreType(storeName);
            qztApplyTryorder.setOrderNo(OrderUtil.generateOrderNo("SY"));
            qztApplyTryorder.setStoreName(storeTel);
            qztApplyTryorder.setDiseaseType(diseaseType);
            qztApplyTryorder.setDiseaseName(diseaseName);
            qztApplyTryorder.setBusId(busId);
            qztApplyTryorder.setBusName(busName);
            if (referrerUserId != null) {
                qztApplyTryorder.setReferrerUserId(referrerUserId);
            }
            qztApplyTryorder.setCreateTime(new Date());
            qztApplyTryorder.setApplyUserId(userId);
            qztApplyTryorder.setOrderState("01");
            if (service.insert(qztApplyTryorder)) {
                return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
            }
            return returnUtil.returnMess(Constant.DATA_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "取消订单", notes = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyTryorder/cancelOrder")
    public Map<String, Object> cancelOrder(String vno, String cno, String orderId, String tokenId, Long userId) {
        try {
            Map dataMap = new HashMap();
            dataMap.put("id", orderId);
            dataMap.put("applyUserId", userId);
            dataMap.put("oldOrderState", "01");
            dataMap.put("orderState", "99");
            if (service.updateTryOrder(dataMap)) {
                QztApplyTryorder qztApplyTryorder = service.queryById(Long.parseLong(orderId));
                this.qztBusLogService.addBusLog("07", "已取消", qztApplyTryorder.getOrderNo(), "取消订单", userId);
                return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
            }
            return returnUtil.returnMess(Constant.DATA_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


    @ApiOperation(value = "同意订单", notes = "同意订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyTryorder/aggreeOrder")
    public Map<String, Object> aggreeOrder(String vno, String cno, String orderId, String tokenId, Long userId) {
        try {
            Map dataMap = new HashMap();
            dataMap.put("id", orderId);
            dataMap.put("busId", userId);
            dataMap.put("oldOrderState", "01");
            dataMap.put("orderState", "03");
            if (service.updateTryOrder(dataMap)) {
                QztApplyTryorder qztApplyTryorder = service.queryById(Long.parseLong(orderId));
                this.qztBusLogService.addBusLog("07", "待到店", qztApplyTryorder.getOrderNo(), "待到店", userId);
                return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
            }
            return returnUtil.returnMess(Constant.DATA_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "拒绝订单", notes = "拒绝订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyTryorder/refuseOrder")
    public Map<String, Object> refuseOrder(String vno, String cno, String orderId, String tokenId, Long userId) {
        try {
            Map dataMap = new HashMap();
            dataMap.put("id", orderId);
            dataMap.put("busId", userId);
            dataMap.put("oldOrderState", "01");
            dataMap.put("orderState", "90");
            if (service.updateTryOrder(dataMap)) {
                QztApplyTryorder qztApplyTryorder = service.queryById(Long.parseLong(orderId));
                this.qztBusLogService.addBusLog("07", "已拒绝", qztApplyTryorder.getOrderNo(), "拒绝订单", userId);
                return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
            }
            return returnUtil.returnMess(Constant.DATA_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "获得预约订单基本信息", notes = "获得预约订单基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyTryorder/getTryOrderMess")
    public Map<String, Object> getTryOrderMess(String vno, String cno, String orderId, String tokenId, Long userId) {
        try {
            Map dataMap = new HashMap();
            QztApplyTryorder qztApplyTryorder = service.queryById(Long.parseLong(orderId));
            qztApplyTryorder.setUpdateTime("07".equals(qztApplyTryorder.getOrderState()) ? qztApplyTryorder.getUpdateTime() : null);
            dataMap.put("orderData", qztApplyTryorder);
            Map params = new HashMap();
            //获得图片信息
            params.put("busId", orderId);
            params.put("busType", "01");
            params.put("state", "0");
            List<QztBusPic> qztBusPics = qztBusPicService.getBusPics(params);//获得试用前图片信息

            params.put("busType", "02");
            List<QztBusPic> qztBusPics1 = qztBusPicService.getBusPics(params);//获得试用后图片信息
            dataMap.put("qztBusPics", qztBusPics);
            dataMap.put("qztBusPics1", qztBusPics1);
            dataMap.put("stateMc", DicParamUtil.getDicCodeByType("TRY_STATE", qztApplyTryorder.getOrderState()));
            return returnUtil.returnMess(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "已到店订单", notes = "已到店订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cardId", value = "身份证号码", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "imgAr", value = "图片", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyTryorder/qrddOrder")
    public Map<String, Object> qrddOrder(String vno, String cno, String orderId, String tokenId, Long userId, String cardId, String[] imgAr) {
        try {
            //检验身份证是否已经存在
            QztApplyTryorder qztApplyTryorder = service.queryById(Long.parseLong(orderId));
            List rs = service.queryOrderByCradId(cardId, qztApplyTryorder.getDiseaseType());
            if (!rs.isEmpty() && rs.size() > 0) {
                return returnUtil.returnMess(Constant.CARD_REGISTER);
            }
            Map dataMap = new HashMap();
            dataMap.put("id", orderId);
            dataMap.put("busId", userId);
            dataMap.put("oldOrderState", "03");
            dataMap.put("cardId", cardId);
            dataMap.put("orderState", "05");
            if (service.updateTryOrder(dataMap)) {
                //保存图片
                int index = 1;
                List<QztBusPic> pics = new ArrayList<>();
                for (String str : imgAr) {
                    QztBusPic qztBusPic = new QztBusPic();
                    qztBusPic.setBusType("01");
                    qztBusPic.setBusId(Long.parseLong(orderId));
                    qztBusPic.setBannerUrl(str);
                    qztBusPic.setCreateTime(new Date());
                    qztBusPic.setOrderNum(index++);
                    qztBusPic.setState("0");
                    pics.add(qztBusPic);
                }
                qztBusPicService.saveBusPics(pics);
                this.qztBusLogService.addBusLog("07", "已到店", qztApplyTryorder.getOrderNo(), "客户到店", userId);
                return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
            }
            return returnUtil.returnMess(Constant.DATA_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "已完成", notes = "已完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "imgAr", value = "图片", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyTryorder/successOrder")
    public Map<String, Object> successOrder(String vno, String cno, String orderId, String tokenId, Long userId, String[] imgAr) {
        try {
            //检验身份证是否已经存在
            Map dataMap = new HashMap();
            dataMap.put("id", orderId);
            dataMap.put("busId", userId);
            dataMap.put("oldOrderState", "05");
            dataMap.put("orderState", "07");
            dataMap.put("updateTime", new Date());
            if (service.updateTryOrder(dataMap)) {
                QztApplyTryorder qztApplyTryorder = service.queryById(Long.parseLong(orderId));
                //保存图片
                int index = 1;
                List<QztBusPic> pics = new ArrayList<>();
                for (String str : imgAr) {
                    QztBusPic qztBusPic = new QztBusPic();
                    qztBusPic.setBusType("02");
                    qztBusPic.setBusId(Long.parseLong(orderId));
                    qztBusPic.setBannerUrl(str);
                    qztBusPic.setOrderNum(index++);
                    qztBusPic.setState("0");
                    pics.add(qztBusPic);
                }
                qztBusPicService.saveBusPics(pics);
                this.qztBusLogService.addBusLog("07", "已完成", qztApplyTryorder.getOrderNo(), "已完成", userId);
                return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
            }
            return returnUtil.returnMess(Constant.DATA_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


    @ApiOperation(value = "检测身份证是否已经试用", notes = "检测身份证是否已经试用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cardId", value = "身份证号码", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", dataType = "String", required = false, paramType = "query"),

    })
    @PostMapping("/webapi/qztApplyTryorder/checkCardId")
    public Map<String, Object> checkCardId(String vno, String cno, String tokenId, String cardId, String orderId) {

        try {
            if (StringUtil.isEmpty(cardId)) {
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
            String disType = "";
            if (!StringUtil.isEmpty(orderId)) {
                QztApplyTryorder qztApplyTryorder = service.queryById(Long.parseLong(orderId));
                disType = qztApplyTryorder.getDiseaseType();
            }
            //检验身份证是否已经存在
            List rs = service.queryOrderByCradId(cardId, disType);
            if (!rs.isEmpty() && rs.size() > 0) {
                return returnUtil.returnMess(Constant.CARD_REGISTER);
            }
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}

