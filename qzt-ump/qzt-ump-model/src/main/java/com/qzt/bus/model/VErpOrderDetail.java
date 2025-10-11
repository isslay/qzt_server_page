package com.qzt.bus.model;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author snow
 * @since 2023-10-23
 */
@TableName("v_erp_order_detail")
public class VErpOrderDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 单据ID
     */
	@TableField("bill_no")
	private String billNo;
    /**
     * 零售单明细序号
     */
	@TableField("bill_sn")
	private String billSn;
    /**
     * 商品编号
     */
	@TableField("goods_code")
	private String goodsCode;
    /**
     * 商品ID
     */
	@TableField("goods_id")
	private String goodsId;
    /**
     * 商品名称
     */
	@TableField("goods_name")
	private String goodsName;
    /**
     * 批准文号
     */
	@TableField("approval_no")
	private String approvalNo;
    /**
     * 规格/型号
     */
	@TableField("goods_spec")
	private String goodsSpec;
    /**
     * 生产厂家
     */
	private String manufacturer;
    /**
     * 单位
     */
	private String unit;
    /**
     * 数量
     */
	private BigDecimal num;
    /**
     * 零售价
     */
	@TableField("retail_p")
	private BigDecimal retailP;
    /**
     * 实价
     */
	@TableField("tra_price")
	private BigDecimal traPrice;
    /**
     * 应收金额
     */
	@TableField("rec_amt")
	private BigDecimal recAmt;
    /**
     * 实收金额
     */
	@TableField("paid_in_amt")
	private BigDecimal paidInAmt;
    /**
     * 金额
     */
	private BigDecimal amount;
    /**
     * 批号/序列号
     */
	@TableField("batch_Code")
	private String batchCode;
    /**
     * 生产日期
     */
	@TableField("produce_date")
	private String produceDate;
    /**
     * 有效期至
     */
	@TableField("val_date")
	private String valDate;
    /**
     * 商品产地
     */
	private String place;
    /**
     * 批次号
     */
	@TableField("batch_no")
	private String batchNo;
    /**
     * 税额
     */
	private BigDecimal tax;
    /**
     * 扣率
     */
	@TableField("ded_rate")
	private BigDecimal dedRate;
    /**
     * 税率
     */
	private BigDecimal rate;
    /**
     * 最新进项税率
     */
	@TableField("in_tax_rate")
	private BigDecimal inTaxRate;
    /**
     * 最新销项税率
     */
	@TableField("out_tax_rate")
	private BigDecimal outTaxRate;
    /**
     * 成本单价
     */
	private BigDecimal cost;
    /**
     * 成本金额
     */
	@TableField("cost_amt")
	private BigDecimal costAmt;
    /**
     * 毛利
     */
	private BigDecimal profit;
    /**
     * 毛利率(%)
     */
	@TableField("profit_rate")
	private BigDecimal profitRate;
    /**
     * 含税成本价
     */
	@TableField("tax_price")
	private BigDecimal taxPrice;
    /**
     * 含税成本金额
     */
	@TableField("tax_cost_amt")
	private BigDecimal taxCostAmt;
    /**
     * 含税毛利
     */
	@TableField("tax_profit")
	private BigDecimal taxProfit;
    /**
     * 含税毛利率
     */
	@TableField("tax_profit_rate")
	private BigDecimal taxProfitRate;
    /**
     * 销售员
     */
	@TableField("sale_man_name")
	private String saleManName;
    /**
     * 是否处方药
     */
	@TableField("is_pres")
	private String isPres;
    /**
     * 处方分类
     */
	@TableField("recipe_type")
	private String recipeType;
    /**
     * 供货商编号
     */
	@TableField("business_code")
	private String businessCode;
    /**
     * 供货单位
     */
	@TableField("business_name")
	private String businessName;
    /**
     * 三支柱分类
     */
	@TableField("san_zz")
	private String sanZz;
    /**
     * 采购属性
     */
	private String cgsx;
    /**
     * 销售类别
     */
	@TableField("sale_lb")
	private String saleLb;
    /**
     * 结构分类
     */
	private String jgfl;
    /**
     * 商家优惠
     */
	@TableField("merchant_discount")
	private BigDecimal merchantDiscount;
    /**
     * 平台优惠
     */
	@TableField("platform_discount")
	private BigDecimal platformDiscount;
    /**
     * 运费优惠
     */
	@TableField("freight_discount")
	private BigDecimal freightDiscount;
    /**
     * 运费优惠商家承担
     */
	@TableField("freight_discount_business")
	private BigDecimal freightDiscountBusiness;
    /**
     * 运费优惠平台承担
     */
	@TableField("freight_discount_platform")
	private BigDecimal freightDiscountPlatform;
    /**
     * 平台服务费
     */
	@TableField("platform_fee")
	private BigDecimal platformFee;
    /**
     * 履约服务费
     */
	@TableField("agreement_fee")
	private BigDecimal agreementFee;
    /**
     * 支付服务费
     */
	@TableField("pay_fee")
	private BigDecimal payFee;
    /**
     * 呼单配送费
     */
	@TableField("call_delivery_fee")
	private BigDecimal callDeliveryFee;
    /**
     * 呼单小费
     */
	@TableField("call_tip_fee")
	private BigDecimal callTipFee;
    /**
     * 维度ID
     */
	@TableField("angle_id")
	private String angleId;
    /**
     * 一级分类
     */
	@TableField("spfl_a")
	private String spflA;
    /**
     * 二级分类
     */
	@TableField("spfl_b")
	private String spflB;
    /**
     * 三级分类
     */
	@TableField("spfl_c")
	private String spflC;
    /**
     * 四级分类
     */
	@TableField("spfl_d")
	private String spflD;
    /**
     * 订单渠道
     */
	@TableField("order_channel")
	private String orderChannel;
    /**
     * 订单来源
     */
	@TableField("order_source")
	private String orderSource;
    /**
     * 经营类别
     */
	@TableField("g_category")
	private String gCategory;
    /**
     * 质量类别
     */
	@TableField("q_category")
	private String qCategory;
    /**
     * 日期
     */
	private String dates;
    /**
     * 机构名称
     */
	@TableField("org_name")
	private String orgName;
    /**
     * 单据编号
     */
	@TableField("bill_code")
	private String billCode;
    /**
     * 机构ID
     */
	@TableField("org_id")
	private String orgId;
    /**
     * 患者姓名
     */
	private String contact;
    /**
     * 患者手机号
     */
	private String mobile;
    /**
     * 开方医院名称
     */
	@TableField("hospital_name")
	private String hospitalName;
    /**
     * 开方医生姓名
     */
	@TableField("open_phy_name")
	private String openPhyName;
    /**
     * 年龄
     */
	private String age;
    /**
     * 性别
     */
	private String gender;
    /**
     * 所患疾病
     */
	@TableField("suffer_dis")
	private String sufferDis;
    /**
     * 备注说明
     */
	private String remark;
	@TableField("created_at")
	private Date createdAt;
	@TableField("updated_at")
	private Date updatedAt;
    /**
     * 优惠券JOB执行时需要
     */
	@TableField("coupon_date")
	private Date couponDate;


	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillSn() {
		return billSn;
	}

	public void setBillSn(String billSn) {
		this.billSn = billSn;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getApprovalNo() {
		return approvalNo;
	}

	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}

	public String getGoodsSpec() {
		return goodsSpec;
	}

	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public BigDecimal getRetailP() {
		return retailP;
	}

	public void setRetailP(BigDecimal retailP) {
		this.retailP = retailP;
	}

	public BigDecimal getTraPrice() {
		return traPrice;
	}

	public void setTraPrice(BigDecimal traPrice) {
		this.traPrice = traPrice;
	}

	public BigDecimal getRecAmt() {
		return recAmt;
	}

	public void setRecAmt(BigDecimal recAmt) {
		this.recAmt = recAmt;
	}

	public BigDecimal getPaidInAmt() {
		return paidInAmt;
	}

	public void setPaidInAmt(BigDecimal paidInAmt) {
		this.paidInAmt = paidInAmt;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
	}

	public String getValDate() {
		return valDate;
	}

	public void setValDate(String valDate) {
		this.valDate = valDate;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getDedRate() {
		return dedRate;
	}

	public void setDedRate(BigDecimal dedRate) {
		this.dedRate = dedRate;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getInTaxRate() {
		return inTaxRate;
	}

	public void setInTaxRate(BigDecimal inTaxRate) {
		this.inTaxRate = inTaxRate;
	}

	public BigDecimal getOutTaxRate() {
		return outTaxRate;
	}

	public void setOutTaxRate(BigDecimal outTaxRate) {
		this.outTaxRate = outTaxRate;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getCostAmt() {
		return costAmt;
	}

	public void setCostAmt(BigDecimal costAmt) {
		this.costAmt = costAmt;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public BigDecimal getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(BigDecimal profitRate) {
		this.profitRate = profitRate;
	}

	public BigDecimal getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(BigDecimal taxPrice) {
		this.taxPrice = taxPrice;
	}

	public BigDecimal getTaxCostAmt() {
		return taxCostAmt;
	}

	public void setTaxCostAmt(BigDecimal taxCostAmt) {
		this.taxCostAmt = taxCostAmt;
	}

	public BigDecimal getTaxProfit() {
		return taxProfit;
	}

	public void setTaxProfit(BigDecimal taxProfit) {
		this.taxProfit = taxProfit;
	}

	public BigDecimal getTaxProfitRate() {
		return taxProfitRate;
	}

	public void setTaxProfitRate(BigDecimal taxProfitRate) {
		this.taxProfitRate = taxProfitRate;
	}

	public String getSaleManName() {
		return saleManName;
	}

	public void setSaleManName(String saleManName) {
		this.saleManName = saleManName;
	}

	public String getIsPres() {
		return isPres;
	}

	public void setIsPres(String isPres) {
		this.isPres = isPres;
	}

	public String getRecipeType() {
		return recipeType;
	}

	public void setRecipeType(String recipeType) {
		this.recipeType = recipeType;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getSanZz() {
		return sanZz;
	}

	public void setSanZz(String sanZz) {
		this.sanZz = sanZz;
	}

	public String getCgsx() {
		return cgsx;
	}

	public void setCgsx(String cgsx) {
		this.cgsx = cgsx;
	}

	public String getSaleLb() {
		return saleLb;
	}

	public void setSaleLb(String saleLb) {
		this.saleLb = saleLb;
	}

	public String getJgfl() {
		return jgfl;
	}

	public void setJgfl(String jgfl) {
		this.jgfl = jgfl;
	}

	public BigDecimal getMerchantDiscount() {
		return merchantDiscount;
	}

	public void setMerchantDiscount(BigDecimal merchantDiscount) {
		this.merchantDiscount = merchantDiscount;
	}

	public BigDecimal getPlatformDiscount() {
		return platformDiscount;
	}

	public void setPlatformDiscount(BigDecimal platformDiscount) {
		this.platformDiscount = platformDiscount;
	}

	public BigDecimal getFreightDiscount() {
		return freightDiscount;
	}

	public void setFreightDiscount(BigDecimal freightDiscount) {
		this.freightDiscount = freightDiscount;
	}

	public BigDecimal getFreightDiscountBusiness() {
		return freightDiscountBusiness;
	}

	public void setFreightDiscountBusiness(BigDecimal freightDiscountBusiness) {
		this.freightDiscountBusiness = freightDiscountBusiness;
	}

	public BigDecimal getFreightDiscountPlatform() {
		return freightDiscountPlatform;
	}

	public void setFreightDiscountPlatform(BigDecimal freightDiscountPlatform) {
		this.freightDiscountPlatform = freightDiscountPlatform;
	}

	public BigDecimal getPlatformFee() {
		return platformFee;
	}

	public void setPlatformFee(BigDecimal platformFee) {
		this.platformFee = platformFee;
	}

	public BigDecimal getAgreementFee() {
		return agreementFee;
	}

	public void setAgreementFee(BigDecimal agreementFee) {
		this.agreementFee = agreementFee;
	}

	public BigDecimal getPayFee() {
		return payFee;
	}

	public void setPayFee(BigDecimal payFee) {
		this.payFee = payFee;
	}

	public BigDecimal getCallDeliveryFee() {
		return callDeliveryFee;
	}

	public void setCallDeliveryFee(BigDecimal callDeliveryFee) {
		this.callDeliveryFee = callDeliveryFee;
	}

	public BigDecimal getCallTipFee() {
		return callTipFee;
	}

	public void setCallTipFee(BigDecimal callTipFee) {
		this.callTipFee = callTipFee;
	}

	public String getAngleId() {
		return angleId;
	}

	public void setAngleId(String angleId) {
		this.angleId = angleId;
	}

	public String getSpflA() {
		return spflA;
	}

	public void setSpflA(String spflA) {
		this.spflA = spflA;
	}

	public String getSpflB() {
		return spflB;
	}

	public void setSpflB(String spflB) {
		this.spflB = spflB;
	}

	public String getSpflC() {
		return spflC;
	}

	public void setSpflC(String spflC) {
		this.spflC = spflC;
	}

	public String getSpflD() {
		return spflD;
	}

	public void setSpflD(String spflD) {
		this.spflD = spflD;
	}

	public String getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(String orderChannel) {
		this.orderChannel = orderChannel;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getgCategory() {
		return gCategory;
	}

	public void setgCategory(String gCategory) {
		this.gCategory = gCategory;
	}

	public String getqCategory() {
		return qCategory;
	}

	public void setqCategory(String qCategory) {
		this.qCategory = qCategory;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getOpenPhyName() {
		return openPhyName;
	}

	public void setOpenPhyName(String openPhyName) {
		this.openPhyName = openPhyName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSufferDis() {
		return sufferDis;
	}

	public void setSufferDis(String sufferDis) {
		this.sufferDis = sufferDis;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCouponDate() {
		return couponDate;
	}

	public void setCouponDate(Date couponDate) {
		this.couponDate = couponDate;
	}

	@Override
	public String toString() {
		return "VErpOrderDetail{" +
			", billNo=" + billNo +
			", billSn=" + billSn +
			", goodsCode=" + goodsCode +
			", goodsId=" + goodsId +
			", goodsName=" + goodsName +
			", approvalNo=" + approvalNo +
			", goodsSpec=" + goodsSpec +
			", manufacturer=" + manufacturer +
			", unit=" + unit +
			", num=" + num +
			", retailP=" + retailP +
			", traPrice=" + traPrice +
			", recAmt=" + recAmt +
			", paidInAmt=" + paidInAmt +
			", amount=" + amount +
			", batchCode=" + batchCode +
			", produceDate=" + produceDate +
			", valDate=" + valDate +
			", place=" + place +
			", batchNo=" + batchNo +
			", tax=" + tax +
			", dedRate=" + dedRate +
			", rate=" + rate +
			", inTaxRate=" + inTaxRate +
			", outTaxRate=" + outTaxRate +
			", cost=" + cost +
			", costAmt=" + costAmt +
			", profit=" + profit +
			", profitRate=" + profitRate +
			", taxPrice=" + taxPrice +
			", taxCostAmt=" + taxCostAmt +
			", taxProfit=" + taxProfit +
			", taxProfitRate=" + taxProfitRate +
			", saleManName=" + saleManName +
			", isPres=" + isPres +
			", recipeType=" + recipeType +
			", businessCode=" + businessCode +
			", businessName=" + businessName +
			", sanZz=" + sanZz +
			", cgsx=" + cgsx +
			", saleLb=" + saleLb +
			", jgfl=" + jgfl +
			", merchantDiscount=" + merchantDiscount +
			", platformDiscount=" + platformDiscount +
			", freightDiscount=" + freightDiscount +
			", freightDiscountBusiness=" + freightDiscountBusiness +
			", freightDiscountPlatform=" + freightDiscountPlatform +
			", platformFee=" + platformFee +
			", agreementFee=" + agreementFee +
			", payFee=" + payFee +
			", callDeliveryFee=" + callDeliveryFee +
			", callTipFee=" + callTipFee +
			", angleId=" + angleId +
			", spflA=" + spflA +
			", spflB=" + spflB +
			", spflC=" + spflC +
			", spflD=" + spflD +
			", orderChannel=" + orderChannel +
			", orderSource=" + orderSource +
			", gCategory=" + gCategory +
			", qCategory=" + qCategory +
			", dates=" + dates +
			", orgName=" + orgName +
			", billCode=" + billCode +
			", orgId=" + orgId +
			", contact=" + contact +
			", mobile=" + mobile +
			", hospitalName=" + hospitalName +
			", openPhyName=" + openPhyName +
			", age=" + age +
			", gender=" + gender +
			", sufferDis=" + sufferDis +
			", remark=" + remark +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			", couponDate=" + couponDate +
			"}";
	}
}
