package com.qzt.common.core.constant;

/**
 * ReturnUtil code
 *
 * @author snow
 * @date
 */
public final class Constant {
    /**
     * 操作成功
     */
    public static final String RESULT_CODE_SUCCESS = "200";
    /**
     * 操作失败
     */
    public static final String OPERATION_FAILURE = "0201";
    /**
     * 系统繁忙
     */
    public static final String RESULT_CODE_TIMEOUT = "0001";
    /**
     * 登录超时
     */
    public static final String LOGIN_OUT = "0002";
    /**
     * 数据异常
     */
    public static final String DATA_ERROR = "0003";
    /**
     * 缺少数据项
     */
    public static final String MISSING_DATA_ITEM = "0005";
    /**
     * 图形验证码错误
     */
    public static final String IMG_CODE_ERROR = "0006";
    /**
     * 密码不正确
     */
    public static final String PWD_ERROR = "0007";
    /**
     * 验证码输入错误
     */
    public static final String MOBILECODE_ERROR = "0008";
    /**
     * 会员已注册
     */
    public static final String USER_REG = "0009";
    /**
     * 用户登录失败
     */
    public static final String USER_LOGIN_ERROR = "0010";
    /**
     * 手机号未注册
     */
    public static final String HAVE_SIGN_NO = "0011";
    /**
     * 短信验证码过期
     */
    public static final String MOBILECODE_PASS = "0012";
    /**
     * 上传失败，文件最大上限为500K
     */
    public static final String FILE_UPLOAD_FAILED = "0015";
    /**
     * 您添加的收货地址已超过最高限定数量10个
     */
    public static final String ADDRESS_LIMIT = "6001";
    /**
     * 收货地址错误
     */
    public static final String ADDRESS_ERROR = "6002";
    /**
     * 该商品不存在或已下架
     */
    public static final String GOODS_NOTHINGNESS = "6003";
    /**
     * 库存不足
     */
    public static final String UNDERSTOCK = "6005";
    /**
     * 暂无该城市信息，请联系管理员处理
     */
    public static final String NO_CITY_INFORMATION_AVAILABLE = "6006";
    /**
     * 您添加的收款账号已超过最高限定数量10个
     */
    public static final String USERBANK_LIMIT = "6007";
    /**
     * 余额不足
     */
    public static final String BALANCE_INSUFFICIENT = "6008";
    /**
     * 请设置支付密码
     */
    public static final String PLEASE_SET_THE_PAYMENT_PASSWORD = "6009";
    /**
     * 提现金额错误
     */
    public static final String WITHDRAWAL_AMOUNT_ERROR = "6010";
    /**
     * 超过每天提现次数
     */
    public static final String LIMIT_OF_WITHDRAWAL_TIMES = "6011";
    /**
     * 支付密码错误
     */
    public static final String PAY_PWD_ERROR = "6012";
    /**
     * 手续费核算不一致，请重新提交
     */
    public static final String COMMISSION_ERROR = "6015";
    /**
     * 请选择正确的收款账号
     */
    public static final String PAYMENT_ACCOUNT_ERROR = "6016";
    /**
     * 该订单不存在
     */
    public static final String ORDER_DOES_NOT_EXIST = "6017";
    /**
     * 该订单已完成，请前往订单列表查看
     */
    public static final String ORDER_OFF_THE_STOCKS = "6018";
    /**
     * 请选择正确的支付方式
     */
    public static final String CHOOSE_THE_RIGHT_PAYMENT_METHOD = "6019";
    /**
     * 该商品无法使用抵扣券
     */
    public static final String VOUCHERS_CANNOT_BE_USED_FOR_THE_GOODS = "6020";
    /**
     * 请选择有效抵扣券
     */
    public static final String PLEASE_SELECT_VALID_VOUCHER = "6021";

    /**
     * 当前推荐人不是服务站，继续保存推荐人将无效
     */
    public static final String REFERRER_ITS_NOT_A_SERVICE_STATION = "6022";

    /**
     * 折扣券错误，请重新选择
     */
    public static final String TO_MEET_THE_100 = "6023";
    /**
     * 该服务站已歇业或已下线
     */
    public static final String OFFLINE_OR_OUT_OF_BUSINESS = "6025";

    /**
     * 身份证已注册
     */
    public static final String CARD_REGISTER = "7000";

    /**
     * 手机号已存在
     */
    public static final String MOBILE_REGISTER = "7001";

    /**
     * 已存在推荐人
     */
    public static final String HAVE_REF = "7002";

    /**
     * 推荐人信息有误，不存在或注册时间在您之后
     */
    public static final String CHANGE_REF_ERROR = "7003";

    /**
     * 自己不能激活自己分享的优惠券
     */
    public static final String COUPON_ERROR = "7004";
    /**
     * 对不起券已激活
     */
    public static final String HAVE_AVTIVE_COUPON = "7005";

    public static final String HAVE_TRY = "7006";

    public static final Long ORDER_LOW_NUM = 5000L;

    public static final Integer ORDER_LOW_NUM_INT = 5000;
}
