package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztWithdrawMapper;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.model.QztUserBank;
import com.qzt.bus.model.QztWithdraw;
import com.qzt.bus.rpc.api.*;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.BigDecimalUtil;
import com.qzt.common.tools.DateUtil;
import com.qzt.common.tools.PriceUtil;
import com.qzt.pagedef.PageDef;
import com.qzt.ump.common.LogConstant;
import com.qzt.ump.model.SysParamModel;
import com.qzt.ump.model.SysUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@Service("qztWithdrawService")
public class QztWithdrawServiceImpl extends BaseServiceImpl<QztWithdrawMapper, QztWithdraw> implements IQztWithdrawService {

    @Autowired
    private QztWithdrawMapper qztWithdrawMapper;

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IQztUserBankService qztUserBankService;

    @Autowired
    private IQztAccountService qztAccountService;

    @Autowired
    private IQztBusLogService qztBusLogService;


    @Override
    public Page<QztWithdraw> find(Page<QztWithdraw> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztWithdraw> rb = this.qztWithdrawMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map userWithdrawApplyfor(QztWithdraw qztWithdraw, String payPwd) throws Exception {
        Map remap = new HashMap();
        long withdrawMin = 1000L;//提现最低金额 fen
        long withdrawMax = 10000000L;//提现最高金额 fen
        //验证提现金额
        if (qztWithdraw.getWithdrawMoney() < withdrawMin) {
            remap.put("code", "6010");
            remap.put("message", "提现金额需大于" + PriceUtil.exactlyTwoDecimalPlaces(withdrawMin) + "元");
            return remap;
        } else if (qztWithdraw.getWithdrawMoney() % 100L != 0) {
            remap.put("code", "6010");
            return remap;
        } else if (qztWithdraw.getWithdrawMoney() > withdrawMax) {
            remap.put("code", "6010");
            remap.put("message", "单笔提现金额上限为10万");
            return remap;
        }
        //提现次数处理
        BigDecimal withdrawFrequencyLimit = this.disposeWithdrawFrequencyLimit(qztWithdraw, "WITHDRAW_FREQUENCY_LIMIT");
        if (withdrawFrequencyLimit.compareTo(BigDecimal.ZERO) == 1) {
            remap.put("code", "6011");
            remap.put("message", "超过每天提现次数，每天可提现" + withdrawFrequencyLimit + "次");
            return remap;
        }
        //验证支付密码
        Long result = this.qztUserService.verifyPayPwd(qztWithdraw.getUserId(), payPwd);
        if (result == -1) {
            remap.put("code", "6009");
            return remap;
        } else if (result != 1) {
            remap.put("code", "6012");
            return remap;
        }
        //提现手续费处理
        qztWithdraw = this.disposeWithdrawServiceCharge(qztWithdraw, "WITHDRAW_COMMISSION_RATIO", "WITHDRAW_COMMISSION_LEAST", "WITHDRAW_COMMISSION_MAX");
        if (qztWithdraw == null) {
            remap.put("code", "6015");
            return remap;
        }

        //收款账号处理
        qztWithdraw = this.disposeBank(qztWithdraw);
        if (qztWithdraw == null) {
            remap.put("code", "6016");
            remap.put("message", "收款账号错误，请选择正确的收款账号再提现！");
            return remap;
        }
        qztWithdraw.setAuditState("00");//审核状态默认待审核00

        QztWithdraw withdraw = this.add(qztWithdraw);
        if (withdraw == null) {
            throw new Exception("新增提现记录失败");
        }
        //扣除对应金额
        boolean accounreuslt = this.qztAccountService.updateQztAccount(qztWithdraw.getId().toString(), qztWithdraw.getUserId(), qztWithdraw.getWithdrawMoney(), "05", "11", "提现申请，扣除余额【" + qztWithdraw.getId() + "】");
        if (!accounreuslt) {
            throw new Exception("6008");
        }
        remap.put("message", "提现成功，请等待审核");
        remap.put("code", "200");
        return remap;
    }

    @Override
    public Map batchWithdrawalAudit(Long[] ids, SysUserModel currentUser, String auditState, String auditRemark) throws Exception {
        Map remap = new HashMap();
        //审核日志备注
        String busRemark = "";
        String auditType = "";
        boolean isReject = false;//是否驳回
        if ("01".equals(auditState) || "03".equals(auditState) || "20".equals(auditState)) {//通过
            auditType = "审核通过";
            busRemark = "操作人【" + currentUser.getUserName() + "】,提现申请" + auditType;
        } else {//驳回
            auditType = "审核驳回";
            busRemark = "操作人【" + currentUser.getUserName() + "】,提现申请" + auditType;
            isReject = true;
        }

        //处理无法审核提现记录
        Map maps = new HashMap();
        maps.put("withdrawIdArr", ids);
        List<QztWithdraw> qztWithdrawslist = this.qztWithdrawMapper.find(maps);
        List<Map> mapList = new ArrayList<>();
        for (QztWithdraw qztWithdraw : qztWithdrawslist) {
            if ((("01".equals(auditState) || "11".equals(auditState)) && !"00".equals(qztWithdraw.getAuditState())) ||//受理审核
                    (("03".equals(auditState) || "13".equals(auditState)) && !"01".equals(qztWithdraw.getAuditState())) ||//确认审核
                    (("20".equals(auditState) || "15".equals(auditState)) && !"03".equals(qztWithdraw.getAuditState()))) {//完成审核
                Map map = new HashMap();
                String throwsstr = "因审核状态已为" + DicParamUtil.getDicCodeByType("WITHDRAW_AUDIT_STATE", qztWithdraw.getAuditState());
                map.put("id", qztWithdraw.getId());
                map.put("userId", qztWithdraw.getUserId());//用户ID
                map.put("realName", qztWithdraw.getRealName());//姓名
                map.put("withdrawMoney", qztWithdraw.getWithdrawMoney());//提现金额
                map.put("result", "审核失败");//结果
                map.put("cause", throwsstr);//原因
                mapList.add(map);
            }
        }
        if (mapList.size() > 0) {
            remap.put("code", "0201");
            remap.put("data", mapList);
            return remap;
        }

        //更新提现状态
        Integer result = this.qztWithdrawMapper.batchWithdrawalAudit(ids, currentUser.getId(), currentUser.getUserName(), auditState,
                DateUtil.getFormatDate(new Date()), auditRemark);
        if (result != ids.length) {//处理条数与传入条数不等则失败
            throw new Exception("0201");
        }

        for (Long id : ids) {
            if (isReject) {//驳回退回提现金额
                QztWithdraw qztWithdra = this.queryById(id);
                String remark = "余额";//备注
                boolean accounreuslt = this.qztAccountService.updateQztAccount(qztWithdra.getId().toString(), qztWithdra.getUserId(), qztWithdra.getWithdrawMoney(), "25", "01", "提现驳回，返还" + remark + "【" + qztWithdra.getId() + "】");
                if (!accounreuslt) {
                    throw new Exception("0201");
                }
            }
            //添加日志
            this.qztBusLogService.addBusLog("03", DicParamUtil.getDicCodeByType("WITHDRAW_AUDIT_STATE", auditState), LogConstant.WITHDRAW_AUDIT + id.toString(), busRemark + "，id：" + id, currentUser.getId());
        }
        remap.put("code", "200");
        return remap;
    }

    /**
     * 提现收款账号信息处理
     *
     * @param qztWithdraw
     * @return com.qzt.bus.model.QztWithdraw
     * @author Xiaofei
     * @date 2019-11-12
     */
    private QztWithdraw disposeBank(QztWithdraw qztWithdraw) {
        //获取银行卡信息
        QztUserBank qztUserBank = this.qztUserBankService.queryById(qztWithdraw.getCardId());
        if (qztUserBank == null || !qztUserBank.getUserId().equals(qztWithdraw.getUserId())) {
            return null;
        }
        qztWithdraw.setCardType(qztUserBank.getBankType());//账号类型
        qztWithdraw.setCardCode(qztUserBank.getCardNum());//银行卡号
        qztWithdraw.setRealName(qztUserBank.getRealName());//姓名
        qztWithdraw.setBankName(qztUserBank.getBankName());//银行名称
        qztWithdraw.setOpeningBank(qztUserBank.getBankBranchName());//开户行信息
        qztWithdraw.setUserTel(qztUserBank.getBindingTel());//手机号
        return qztWithdraw;
    }

    /**
     * 提现次数每天-计算
     *
     * @param qztWithdraw
     * @return com.qzt.bus.model.QztWithdraw
     * @author Xiaofei
     * @date 2019-07-12
     */
    private BigDecimal disposeWithdrawFrequencyLimit(QztWithdraw qztWithdraw, String key) {
        BigDecimal withdrawFrequencyLimit = BigDecimal.ZERO;
        SysParamModel sysParamModell = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value() + key);//提现次数每天
        //提现次数每天
        if (sysParamModell != null) {
            withdrawFrequencyLimit = new BigDecimal(sysParamModell.getParamValue());
            Long withdrawNum = this.qztWithdrawMapper.verifyWithdrawFrequencyLimit(qztWithdraw.getUserId(), DateUtil.chineseDateFormat.format(new Date()));
            if (withdrawFrequencyLimit.compareTo(BigDecimal.ZERO) > -1 && new BigDecimal(withdrawNum).compareTo(withdrawFrequencyLimit) > -1) {
                return withdrawFrequencyLimit;
            }
        }
        return withdrawFrequencyLimit;
    }

    /**
     * 提现手续费处理
     *
     * @param qztWithdraw
     * @param key
     * @return com.qzt.bus.model.QztWithdraw
     * @author Xiaofei
     * @date 2019-11-12
     */
    private QztWithdraw disposeWithdrawServiceCharge(QztWithdraw qztWithdraw, String key, String key2, String key3) {
        SysParamModel sysParamModelr = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value() + key);//提现手续费比例
        SysParamModel sysParamModell = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value() + key2);//提现手续费最低金额
        SysParamModel sysParamModelm = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value() + key3);//提现手续费最高金额
        BigDecimal commissionRatio = BigDecimal.ZERO;//提现手续费比例（百分比整数）
        long commissionLeast = 0L;//提现最低手续费金额 单位：分
        long commissionMax = 0L;//提现最高手续费金额 单位：分
        if (sysParamModelr != null) {
            commissionRatio = new BigDecimal(sysParamModelr.getParamValue());
        }
        if (sysParamModell != null) {
            commissionLeast = Long.valueOf(sysParamModelr.getParamValue());
        }
        if (sysParamModelm != null) {
            commissionMax = Long.valueOf(sysParamModelr.getParamValue());
        }
        commissionRatio = commissionRatio.divide(new BigDecimal(100), 2, ROUND_HALF_UP);//换算为小数-四舍五入
        BigDecimal withdrawMoneyc = BigDecimal.valueOf(qztWithdraw.getWithdrawMoney());//提现金额
        long serviceCharge = Long.valueOf(commissionRatio.multiply(withdrawMoneyc).setScale(0, BigDecimal.ROUND_DOWN).toString());//手续费金额
        long arrivalAmount = qztWithdraw.getWithdrawMoney() - serviceCharge;//实际到账金额
        serviceCharge = commissionLeast > serviceCharge ? commissionLeast : serviceCharge;//计算最低手续费金额
        serviceCharge = commissionMax < serviceCharge ? serviceCharge : commissionMax;//计算最高手续费金额
        if (serviceCharge != qztWithdraw.getServiceCharge()) {//手续费不一致
            return null;
        }
        qztWithdraw.setServiceCharge(serviceCharge);//手续费金额
        qztWithdraw.setArrivalAmount(arrivalAmount);//实际到账金额
        qztWithdraw.setServiceRatio(commissionRatio);//提现手续费比例
        return qztWithdraw;
    }


    @Override
    public List<Map<String, Object>> userWithdrawExcel(Map map) {
        List<Map<String, Object>> retmaplist = new ArrayList();
        List<QztWithdraw> rb = this.qztWithdrawMapper.find(map);
        String cardType = map.get("cardType").toString();
        for (QztWithdraw qztWithdraw : rb) {
            Map<String, Object> linkmaps = new LinkedHashMap();
            linkmaps.put("createTime", DateUtil.dateTimeFormat.format(qztWithdraw.getCreateTime()));
            linkmaps.put("userId", qztWithdraw.getUserId());
            linkmaps.put("realName", qztWithdraw.getRealName());
            if ("01".equals(cardType)) {
                linkmaps.put("bankName", qztWithdraw.getBankName());
                linkmaps.put("cardCode", qztWithdraw.getCardCode());
                linkmaps.put("userTel", qztWithdraw.getUserTel());
                linkmaps.put("openingBank", qztWithdraw.getOpeningBank());
            } else if ("02".equals(cardType)) {
                linkmaps.put("userTel", qztWithdraw.getUserTel());
                linkmaps.put("cardCode", qztWithdraw.getCardCode());
            }
            linkmaps.put("withdrawMoney", PriceUtil.moneyLongToDecimal(qztWithdraw.getWithdrawMoney()));
            linkmaps.put("serviceCharge", PriceUtil.moneyLongToDecimal(qztWithdraw.getServiceCharge()));
            linkmaps.put("serviceRatio", qztWithdraw.getServiceRatio().multiply(BigDecimal.valueOf(100)) + "%");
            linkmaps.put("arrivalAmount", PriceUtil.moneyLongToDecimal(qztWithdraw.getArrivalAmount()));
            linkmaps.put("auditState", DicParamUtil.getDicCodeByType("WITHDRAW_AUDIT_STATE", qztWithdraw.getAuditState()));
            linkmaps.put("auditUserName", qztWithdraw.getAuditUserName());
            linkmaps.put("auditTime", qztWithdraw.getAuditTime() != null ? DateUtil.dateTimeFormat.format(qztWithdraw.getCreateTime()) : "");
            linkmaps.put("auditRemark", qztWithdraw.getAuditRemark());
            retmaplist.add(linkmaps);
        }
        return retmaplist;
    }


}
