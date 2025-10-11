package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.QztBaseBank;
import com.qzt.bus.rpc.api.IQztBaseBankService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.tools.StringUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztUserBank;
import com.qzt.bus.rpc.api.IQztUserBankService;
import org.springframework.beans.factory.annotation.Autowired;

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
@Api(value = "WebQztUserBankController", description = "收款账号相关Api")
public class WebQztUserBankController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztUserBankService service;

    @Autowired
    private IQztBaseBankService qztBaseBankService;

    /**
     * 查询我的收款账号列表
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "查询我的收款账号列表", notes = "分页查询我的收款账号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztUserBank/getList")
    public Map getList(String vno, String cno, String tokenId, Long userId) {
        try {
            Map remap = new HashMap();
            Map pmap = new HashMap();
            pmap.put("userId", userId);
            pmap.put("isDel", "N");
            List<QztUserBank> qztUserBankList = this.service.findList(pmap);
            List<Map> bankList = new ArrayList<>();
            List<Map> aliPayList = new ArrayList<>();
            for (QztUserBank qztUserBank : qztUserBankList) {
                Map map = this.userBankResult(qztUserBank);
                String bindingTel = (String) map.get("bindingTel");
                map.put("bindingTel", StringUtil.idFormat(bindingTel, 3, 7));
                if ("01".equals(qztUserBank.getBankType())) {//银行卡
                    String cardNum = (String) map.get("cardNum");
                    map.put("cardNum", cardNum.substring(cardNum.length() - 5, cardNum.length() - 1));
                    bankList.add(map);
                } else {
                    String cardNum = (String) map.get("cardNum");
                    map.put("cardNum", cardNum.substring(0, 2) + "*********" + cardNum.substring(cardNum.length() - 3, cardNum.length() - 1));
                    aliPayList.add(map);
                }
            }
            remap.put("bankList", bankList);
            remap.put("aliPayList", aliPayList);
            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 根据id查询收款账号详情
     *
     * @param id
     * @return Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "根据id查询收款账号详情", notes = "根据id查询收款账号详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "银行卡ID", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztUserBank/selectById")
    public Map selectById(String vno, String cno, String tokenId, Long userId, Long id) {
        try {
            QztUserBank qztUserBank = this.service.selectByIdAndUserId(new QztUserBank(id, userId));
            if (qztUserBank == null) {
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
            return returnUtil.returnMessMap(this.userBankResult(qztUserBank));
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 新增收款账号
     *
     * @param entity
     * @param tokenId
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "新增收款账号", notes = "新增收款账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserBank/addUserBank")
    public Map addUserBank(String vno, String cno, String tokenId, QztUserBank entity) {
        try {
            Long resultUserBankSize = this.service.selectUserBankSize(entity.getUserId());
            if (resultUserBankSize >= 10) {
                return returnUtil.returnMess(Constant.USERBANK_LIMIT);
            }
            if (entity.getBankId() != null) {//获取银行信息
                QztBaseBank qztBaseBank = this.qztBaseBankService.queryById(entity.getBankId());
                entity.setBankName(qztBaseBank.getBankName());
                entity.setBankPic(qztBaseBank.getsPic());
                entity.setBankType("01");
            } else {
                entity.setBankName("支付宝");
                entity.setBankPic("https://img.jlqizutang.com/alipay_pic.png");
                entity.setBankType("02");
            }
            entity.setIsDefault("Y".equals(entity.getIsDefault()) ? "Y" : "N");
            entity.setCreateBy(entity.getUserId());
            if ("Y".equals(entity.getIsDefault())) {//如果设为默认执行
                entity.setUpdateBy(entity.getUserId());
                entity.setUpdateTime(new Date());
                this.service.defaultUserBank(entity);
            }
            QztUserBank qztUserBank = this.service.add(entity);
            if (qztUserBank != null) {
                return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS, "新增收款账号成功");
            } else {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "新增收款账号失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 设置默认收款账号
     *
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "设置默认收款账号", notes = "设置默认收款账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserBank/defaultUserBank")
    public Map defaultUserBank(String vno, String cno, String tokenId, Long userId, Long id) {
        try {
            QztUserBank qztUserBank = new QztUserBank();
            qztUserBank.setId(id);
            qztUserBank.setUpdateBy(userId);
            qztUserBank.setUpdateTime(new Date());
            qztUserBank.setUserId(userId);
            this.service.defaultUserBank(qztUserBank);
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 修改收款账号
     *
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "修改收款账号", notes = "修改收款账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pkId", value = "地址ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserBank/modify")
    public Map modify(String vno, String cno, String tokenId, Long pkId, QztUserBank entity) {
        try {
            entity.setUpdateBy(entity.getUserId());
            entity.setUpdateTime(new Date());
            entity.setId(pkId);
            entity.setIsDefault("Y".equals(entity.getIsDefault()) ? "Y" : "N");
            if ("Y".equals(entity.getIsDefault())) {//如果设为默认执行
                entity.setUpdateBy(entity.getUserId());
                entity.setUpdateTime(new Date());
                this.service.defaultUserBank(entity);
            }
            this.service.modifyById(entity);
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS, "修改收款账号成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 删除收款账号
     *
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "删除收款账号", notes = "删除收款账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "收款账号ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserBank/delById")
    public Map delById(String vno, String cno, String tokenId, Long userId, Long id) {
        try {
            this.service.modifyById(new QztUserBank(id, userId, null, "Y"));
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 获取银行列表
     *
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "获取银行列表", notes = "获取银行列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztUserBank/getBankList")
    public Map getBankList(String vno, String cno, String tokenId, Long userId) {
        try {
            List<QztBaseBank> qztBaseBankList = this.qztBaseBankService.findList();
            if ("5".equals(cno)) {//小程序返回数据
                Map remap = new HashMap();
                List<String> bankIdList = new LinkedList<>();
                List<String> bankNameList = new LinkedList<>();
                for (QztBaseBank qztBaseBank : qztBaseBankList) {
                    bankIdList.add(qztBaseBank.getId().toString());
                    bankNameList.add(qztBaseBank.getBankName());
                }
                remap.put("bankIdList", bankIdList);
                remap.put("bankNameList", bankNameList);
                return returnUtil.returnMessMap(remap);
            } else {
                List<Map> data = new ArrayList<>();
                for (QztBaseBank qztBaseBank : qztBaseBankList) {
                    Map mapr = new HashMap();
                    mapr.put("baseBankId", qztBaseBank.getId());
                    mapr.put("baseBankName", qztBaseBank.getBankName());
                    data.add(mapr);
                }
                return returnUtil.returnMess(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 银行信息处理
     *
     * @param qztUserBank
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    private Map userBankResult(QztUserBank qztUserBank) {
        Map remap = new HashMap();
        if (qztUserBank != null) {
            remap.put("id", qztUserBank.getId());
            remap.put("realName", qztUserBank.getRealName());//姓名
            remap.put("bankId", qztUserBank.getBankId());//银行ID
            remap.put("bankName", qztUserBank.getBankName());//银行名称
            remap.put("bankBranchName", qztUserBank.getBankBranchName());
            remap.put("cardNum", qztUserBank.getCardNum());//银行卡号/支付宝账号
            remap.put("bindingTel", qztUserBank.getBindingTel());//手机号
            remap.put("isDefault", qztUserBank.getIsDefault());//是否默认银行卡(是Y、否N)
            remap.put("bankPic", qztUserBank.getBankPic());//logo
        }
        return remap;
    }
}

