package com.qzt.common.web.configuration;

public class CoinPoint {
    public static  int getCoinPoint(String userType){
        int coinVale = 30;
        switch (userType){
            case "20": //注册
                coinVale = 24;
                break;
            case "30": //开通支付钱包
                coinVale = 18;
                break;
            case "40": //开通支付钱包
                coinVale = 12;
                break;
            case "50": //开通支付钱包
                coinVale = 6;
                break;
            default:
                coinVale = 30;
                break;
        }
        return coinVale;
    }

    public static  int[] getVipPoint(int code){
        int[] pointVale = new int[2];
        switch (code){
            case 0:    //消费增加魔法原力0% 6% 9% 12% 15% 18% 21% 24%
                //公益池30% 24%  21% 18% 15% 12% 9% 6%
                pointVale[0] = 0;
                pointVale[1] = 30;
                break;
            case 1:
                pointVale[0] = 6;
                pointVale[1] = 24;
                break;
            case 2:
                pointVale[0] = 9;
                pointVale[1] = 21;
                break;
            case 3:
                pointVale[0] =12;
                pointVale[1] =18;
                break;
            case 4:
                pointVale[0] = 15;
                pointVale[1] = 15;
                break;
            case 5:
                pointVale[0] = 18;
                pointVale[1] = 12;
                break;
            case 6:
                pointVale[0] = 21;
                pointVale[1] = 9;
                break;
            case 7:
                pointVale[0] = 24;
                pointVale[1] = 6;
                break;
            default:
                pointVale[0] = 0;
                pointVale[1] = 0;
                break;
        }
        return pointVale;

    }

    public static  int[] getTypePoine(int code){
        int[] pointVale = new int[2];
        switch (code){
            case 0:
                pointVale[0] = 0;
                pointVale[1] = 30;
                break;
            case 10:
                pointVale[0] = 0;
                pointVale[1] = 30;
                break;
            case 20:
                pointVale[0] = 6;
                pointVale[1] = 24;

                break;
            case 30:
                pointVale[0] =12;
                pointVale[1] =18;
                break;
            case 40:
                pointVale[0] = 18;
                pointVale[1] = 12;
                break;
            case 50:
                pointVale[0] = 24;
                pointVale[1] = 6;
                break;
            default:
                pointVale[0] = 0;
                pointVale[1] = 0;
                break;
        }
        return pointVale;

    }
}
