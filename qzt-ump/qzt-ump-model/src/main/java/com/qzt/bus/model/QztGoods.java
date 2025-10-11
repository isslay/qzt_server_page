package com.qzt.bus.model;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author snow
 * @since 2019-11-05
 */
@TableName("dgm_qzt_goods")
public class QztGoods extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商品类型
     */
	@TableField("goods_type")
	private String goodsType;
    /**
     * 商品名称
     */
	@TableField("goods_name")
	private String goodsName;
    /**
     * 说明
     */
	@TableField("goods_remark")
	private String goodsRemark;
    /**
     * 分享logo
     */
	@TableField("share_pic")
	private String sharePic;
    /**
     * 分享标题
     */
	@TableField("share_title")
	private String shareTitle;
    /**
     * 分享内容
     */
	@TableField("share_content")
	private String shareContent;
    /**
     * 规格说明
     */
	@TableField("goods_spec")
	private String goodsSpec;
    /**
     * 库存
     */
	@TableField("goods_num")
	private Integer goodsNum;
    /**
     * 已售数量
     */
	@TableField("sale_num")
	private Integer saleNum;
    /**
     * 缩略图
     */
	private String thumbnail;
    /**
     * 详情
     */
	private String content;
    /**·
     * 商品单价（单位：分）
     */
	@TableField("goods_price")
	private Long goodsPrice;
	/**·
	 * 商品成本单价（单位：分）
	 */
	@TableField("goods_cost_price")
	private Long goodsCostPrice;
    /**
     * 运费价格（单位：分）
     */
	private Long freight;
    /**
     * 商品状态（0待上架，1上架，2下架,-1删除）
     */
	private String state;
    /**
     * 分享佣金额度
     */
	@TableField("share_money")
	private Long shareMoney;
    /**
     * 推广佣金额度
     */
	@TableField("recommend_money")
	private Long recommendMoney;
    /**
     * 是否需要服务(0是1否)
     */
	@TableField("is_service")
	private String isService;
    /**
     * 服务佣金额度
     */
	@TableField("service_money")
	private Long serviceMoney;
    /**
     * 上架时间
     */
	@TableField("up_time")
	private Date upTime;
    /**
     * 是否使用券(0开1关)
     */
	@TableField("coupon_status")
	private Integer couponStatus;
    /**
     * 排序
     */
	@TableField("type_order")
	private Integer typeOrder;

	/**
	 * 分类id
	 */
	@TableField("class_id")
	private Long classId;
	/**
	 * 分类名称
	 */
	@TableField("class_name")
	private String className;

	public Long getGoodsCostPrice() {
		return goodsCostPrice;
	}

	public void setGoodsCostPrice(Long goodsCostPrice) {
		this.goodsCostPrice = goodsCostPrice;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsRemark() {
		return goodsRemark;
	}

	public void setGoodsRemark(String goodsRemark) {
		this.goodsRemark = goodsRemark;
	}

	public String getSharePic() {
		return sharePic;
	}

	public void setSharePic(String sharePic) {
		this.sharePic = sharePic;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getGoodsSpec() {
		return goodsSpec;
	}

	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Long goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Long getFreight() {
		return freight;
	}

	public void setFreight(Long freight) {
		this.freight = freight;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getShareMoney() {
		return shareMoney;
	}

	public void setShareMoney(Long shareMoney) {
		this.shareMoney = shareMoney;
	}

	public Long getRecommendMoney() {
		return recommendMoney;
	}

	public void setRecommendMoney(Long recommendMoney) {
		this.recommendMoney = recommendMoney;
	}

	public String getIsService() {
		return isService;
	}

	public void setIsService(String isService) {
		this.isService = isService;
	}

	public Long getServiceMoney() {
		return serviceMoney;
	}

	public void setServiceMoney(Long serviceMoney) {
		this.serviceMoney = serviceMoney;
	}

	public Date getUpTime() {
		return upTime;
	}

	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}

	public Integer getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(Integer couponStatus) {
		this.couponStatus = couponStatus;
	}

	public Integer getTypeOrder() {
		return typeOrder;
	}

	public void setTypeOrder(Integer typeOrder) {
		this.typeOrder = typeOrder;
	}

	@Override
	public String toString() {
		return "QztGoods{" +
			", goodsType=" + goodsType +
			", goodsName=" + goodsName +
			", goodsRemark=" + goodsRemark +
			", sharePic=" + sharePic +
			", shareTitle=" + shareTitle +
			", shareContent=" + shareContent +
			", goodsSpec=" + goodsSpec +
			", goodsNum=" + goodsNum +
			", saleNum=" + saleNum +
			", thumbnail=" + thumbnail +
			", content=" + content +
			", goodsPrice=" + goodsPrice +
			", goodsCostPrice=" + goodsCostPrice +
			", freight=" + freight +
			", state=" + state +
			", shareMoney=" + shareMoney +
			", recommendMoney=" + recommendMoney +
			", isService=" + isService +
			", serviceMoney=" + serviceMoney +
			", upTime=" + upTime +
			", couponStatus=" + couponStatus +
			", typeOrder=" + typeOrder +
			", classId=" + classId +
			", className=" + className +
			"}";
	}
}
