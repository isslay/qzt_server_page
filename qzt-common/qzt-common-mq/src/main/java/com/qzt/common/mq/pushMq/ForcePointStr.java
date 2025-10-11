package com.qzt.common.mq.pushMq;

public class ForcePointStr {

    public static String getForceStrPoine(String code) {
        String pointVale = "";
        switch (code) {
            case "01": //注册分享
                pointVale = "ShareMoneyTop";
                break;
            case "02": //完成服务订单
                pointVale = "ServicesMoneyTop";
                break;
            case "06": //用户身份处理
                pointVale = "UserUpgradeTop";
                break;
            default://完成正常订单
                pointVale = "GoodsOrderTop";
                break;
        }
        return pointVale;
    }

}
