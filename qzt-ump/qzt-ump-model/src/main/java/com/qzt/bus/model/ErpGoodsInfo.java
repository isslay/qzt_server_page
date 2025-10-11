package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author Xiaofei
 * @since 2023-08-28
 */
@TableName("erp_goods_info")
public class ErpGoodsInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 机构ID
     */
    @TableId("org_id")
	private String orgId;
    /**
     * 商品ID
     */
	@TableField("goods_id")
	private String goodsId;
    /**
     * 商品编号
     */
	@TableField("goods_code")
	private String goodsCode;
    /**
     * 商品名称
     */
	@TableField("goods_name")
	private String goodsName;
    /**
     * 助记码
     */
	private String logogram;
    /**
     * 规格/型号
     */
	@TableField("goods_spec")
	private String goodsSpec;
    /**
     * 生产厂商
     */
	private String manufacturer;
    /**
     * 包装规格
     */
	private String bzgg;
    /**
     * 商品条码
     */
	@TableField("bar_code")
	private String barCode;
    /**
     * 商品品牌
     */
	@TableField("brand_name")
	private String brandName;
    /**
     * 化学名称
     */
	@TableField("chem_name")
	private String chemName;
    /**
     * 药品本位码
     */
	@TableField("stand_code")
	private String standCode;
    /**
     * 基本单位
     */
	private String unit;
    /**
     * 通用名称
     */
	@TableField("general_name")
	private String generalName;
    /**
     * 保质期方式
     */
	@TableField("life_type")
	private String lifeType;
    /**
     * 保质期单位
     */
	@TableField("day_unit")
	private String dayUnit;
    /**
     * 保质期
     */
	@TableField("in_effect_day")
	private String inEffectDay;
    /**
     * 注册商标
     */
	@TableField("reg_mark")
	private String regMark;
    /**
     * 注册商标效期
     */
	@TableField("reg_mark_validity")
	private String regMarkValidity;
    /**
     * 产地
     */
	private String place;
    /**
     * 商品分类
     */
	@TableField("goods_lm")
	private String goodsLm;
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
     * 受托方
     */
	@TableField("entrust_name")
	private String entrustName;
    /**
     * 受托方地址
     */
	@TableField("entrust_address")
	private String entrustAddress;
    /**
     * 生产地址
     */
	private String scdz;
    /**
     * 新品上报来源
     */
	@TableField("new_rep_origin")
	private String newRepOrigin;
    /**
     * 三支柱分类
     */
	@TableField("san_zz")
	private String sanZz;
    /**
     * 税务分类编码
     */
	@TableField("swfl_code")
	private String swflCode;
    /**
     * 参考标识
     */
	private String ckbs;
    /**
     * 定位标识
     */
	private String dwbs;
    /**
     * 运营分类
     */
	private String yyfl;
    /**
     * 运营二级分类
     */
	@TableField("yyfl_b")
	private String yyflB;
    /**
     * 运营三级分类
     */
	@TableField("yyfl_c")
	private String yyflC;
    /**
     * 运营四级分类
     */
	@TableField("yyfl_d")
	private String yyflD;
    /**
     * 结构分类
     */
	private String jgfl;
    /**
     * 财税商品分类
     */
	private String csspfl;
    /**
     * 注册日期
     */
	@TableField("reg_dates")
	private String regDates;
    /**
     * 医保对应名称
     */
	@TableField("kk_ybmc")
	private String kkYbmc;
    /**
     * 医保项目代码
     */
	@TableField("med_ins_code")
	private String medInsCode;
    /**
     * 医保项目名称
     */
	@TableField("med_ins_name")
	private String medInsName;
    /**
     * 医保类型
     */
	private String yblb;
    /**
     * 含税进价
     */
	@TableField("pur_tax_p")
	private BigDecimal purTaxP;
    /**
     * 进项税率
     */
	@TableField("in_tax_rate")
	private BigDecimal inTaxRate;
    /**
     * 标准进价
     */
	@TableField("pur_p")
	private BigDecimal purP;
    /**
     * 中标价
     */
	@TableField("bidd_price")
	private BigDecimal biddPrice;
    /**
     * 含税售价
     */
	@TableField("sale_tax_p")
	private BigDecimal saleTaxP;
    /**
     * 销项税率
     */
	@TableField("out_tax_rate")
	private BigDecimal outTaxRate;
    /**
     * 标准售价
     */
	@TableField("sale_p")
	private BigDecimal saleP;
    /**
     * 零售价
     */
	@TableField("retail_p")
	private BigDecimal retailP;
    /**
     * 最高零售价
     */
	@TableField("max_retail_p")
	private BigDecimal maxRetailP;
    /**
     * 会员价
     */
	@TableField("mem_price")
	private BigDecimal memPrice;
    /**
     * 录入人
     */
	@TableField("creator_name")
	private String creatorName;
    /**
     * 修改人
     */
	@TableField("last_stf_name")
	private String lastStfName;
    /**
     * 修改时间
     */
	@TableField("last_rev_time")
	private String lastRevTime;
    /**
     * 单次用量单位
     */
	@TableField("mi_dosage_unit")
	private String miDosageUnit;
    /**
     * 单次用量
     */
	@TableField("mi_dosage")
	private String miDosage;
    /**
     * 医保换算率
     */
	@TableField("mi_conversion")
	private String miConversion;
    /**
     * 最小包装
     */
	@TableField("mi_min_package")
	private String miMinPackage;
    /**
     * 天数
     */
	@TableField("mi_days")
	private String miDays;
    /**
     * 医保用法
     */
	@TableField("mi_how_code")
	private String miHowCode;
    /**
     * 医保剂型
     */
	@TableField("mi_dose")
	private String miDose;
    /**
     * 自动拉取购药记录的最后日期(不包含)
     */
	@TableField("last_pull_date")
	private Date lastPullDate;


	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getLogogram() {
		return logogram;
	}

	public void setLogogram(String logogram) {
		this.logogram = logogram;
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

	public String getBzgg() {
		return bzgg;
	}

	public void setBzgg(String bzgg) {
		this.bzgg = bzgg;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getChemName() {
		return chemName;
	}

	public void setChemName(String chemName) {
		this.chemName = chemName;
	}

	public String getStandCode() {
		return standCode;
	}

	public void setStandCode(String standCode) {
		this.standCode = standCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getGeneralName() {
		return generalName;
	}

	public void setGeneralName(String generalName) {
		this.generalName = generalName;
	}

	public String getLifeType() {
		return lifeType;
	}

	public void setLifeType(String lifeType) {
		this.lifeType = lifeType;
	}

	public String getDayUnit() {
		return dayUnit;
	}

	public void setDayUnit(String dayUnit) {
		this.dayUnit = dayUnit;
	}

	public String getInEffectDay() {
		return inEffectDay;
	}

	public void setInEffectDay(String inEffectDay) {
		this.inEffectDay = inEffectDay;
	}

	public String getRegMark() {
		return regMark;
	}

	public void setRegMark(String regMark) {
		this.regMark = regMark;
	}

	public String getRegMarkValidity() {
		return regMarkValidity;
	}

	public void setRegMarkValidity(String regMarkValidity) {
		this.regMarkValidity = regMarkValidity;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getGoodsLm() {
		return goodsLm;
	}

	public void setGoodsLm(String goodsLm) {
		this.goodsLm = goodsLm;
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

	public String getEntrustName() {
		return entrustName;
	}

	public void setEntrustName(String entrustName) {
		this.entrustName = entrustName;
	}

	public String getEntrustAddress() {
		return entrustAddress;
	}

	public void setEntrustAddress(String entrustAddress) {
		this.entrustAddress = entrustAddress;
	}

	public String getScdz() {
		return scdz;
	}

	public void setScdz(String scdz) {
		this.scdz = scdz;
	}

	public String getNewRepOrigin() {
		return newRepOrigin;
	}

	public void setNewRepOrigin(String newRepOrigin) {
		this.newRepOrigin = newRepOrigin;
	}

	public String getSanZz() {
		return sanZz;
	}

	public void setSanZz(String sanZz) {
		this.sanZz = sanZz;
	}

	public String getSwflCode() {
		return swflCode;
	}

	public void setSwflCode(String swflCode) {
		this.swflCode = swflCode;
	}

	public String getCkbs() {
		return ckbs;
	}

	public void setCkbs(String ckbs) {
		this.ckbs = ckbs;
	}

	public String getDwbs() {
		return dwbs;
	}

	public void setDwbs(String dwbs) {
		this.dwbs = dwbs;
	}

	public String getYyfl() {
		return yyfl;
	}

	public void setYyfl(String yyfl) {
		this.yyfl = yyfl;
	}

	public String getYyflB() {
		return yyflB;
	}

	public void setYyflB(String yyflB) {
		this.yyflB = yyflB;
	}

	public String getYyflC() {
		return yyflC;
	}

	public void setYyflC(String yyflC) {
		this.yyflC = yyflC;
	}

	public String getYyflD() {
		return yyflD;
	}

	public void setYyflD(String yyflD) {
		this.yyflD = yyflD;
	}

	public String getJgfl() {
		return jgfl;
	}

	public void setJgfl(String jgfl) {
		this.jgfl = jgfl;
	}

	public String getCsspfl() {
		return csspfl;
	}

	public void setCsspfl(String csspfl) {
		this.csspfl = csspfl;
	}

	public String getRegDates() {
		return regDates;
	}

	public void setRegDates(String regDates) {
		this.regDates = regDates;
	}

	public String getKkYbmc() {
		return kkYbmc;
	}

	public void setKkYbmc(String kkYbmc) {
		this.kkYbmc = kkYbmc;
	}

	public String getMedInsCode() {
		return medInsCode;
	}

	public void setMedInsCode(String medInsCode) {
		this.medInsCode = medInsCode;
	}

	public String getMedInsName() {
		return medInsName;
	}

	public void setMedInsName(String medInsName) {
		this.medInsName = medInsName;
	}

	public String getYblb() {
		return yblb;
	}

	public void setYblb(String yblb) {
		this.yblb = yblb;
	}

	public BigDecimal getPurTaxP() {
		return purTaxP;
	}

	public void setPurTaxP(BigDecimal purTaxP) {
		this.purTaxP = purTaxP;
	}

	public BigDecimal getInTaxRate() {
		return inTaxRate;
	}

	public void setInTaxRate(BigDecimal inTaxRate) {
		this.inTaxRate = inTaxRate;
	}

	public BigDecimal getPurP() {
		return purP;
	}

	public void setPurP(BigDecimal purP) {
		this.purP = purP;
	}

	public BigDecimal getBiddPrice() {
		return biddPrice;
	}

	public void setBiddPrice(BigDecimal biddPrice) {
		this.biddPrice = biddPrice;
	}

	public BigDecimal getSaleTaxP() {
		return saleTaxP;
	}

	public void setSaleTaxP(BigDecimal saleTaxP) {
		this.saleTaxP = saleTaxP;
	}

	public BigDecimal getOutTaxRate() {
		return outTaxRate;
	}

	public void setOutTaxRate(BigDecimal outTaxRate) {
		this.outTaxRate = outTaxRate;
	}

	public BigDecimal getSaleP() {
		return saleP;
	}

	public void setSaleP(BigDecimal saleP) {
		this.saleP = saleP;
	}

	public BigDecimal getRetailP() {
		return retailP;
	}

	public void setRetailP(BigDecimal retailP) {
		this.retailP = retailP;
	}

	public BigDecimal getMaxRetailP() {
		return maxRetailP;
	}

	public void setMaxRetailP(BigDecimal maxRetailP) {
		this.maxRetailP = maxRetailP;
	}

	public BigDecimal getMemPrice() {
		return memPrice;
	}

	public void setMemPrice(BigDecimal memPrice) {
		this.memPrice = memPrice;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getLastStfName() {
		return lastStfName;
	}

	public void setLastStfName(String lastStfName) {
		this.lastStfName = lastStfName;
	}

	public String getLastRevTime() {
		return lastRevTime;
	}

	public void setLastRevTime(String lastRevTime) {
		this.lastRevTime = lastRevTime;
	}

	public String getMiDosageUnit() {
		return miDosageUnit;
	}

	public void setMiDosageUnit(String miDosageUnit) {
		this.miDosageUnit = miDosageUnit;
	}

	public String getMiDosage() {
		return miDosage;
	}

	public void setMiDosage(String miDosage) {
		this.miDosage = miDosage;
	}

	public String getMiConversion() {
		return miConversion;
	}

	public void setMiConversion(String miConversion) {
		this.miConversion = miConversion;
	}

	public String getMiMinPackage() {
		return miMinPackage;
	}

	public void setMiMinPackage(String miMinPackage) {
		this.miMinPackage = miMinPackage;
	}

	public String getMiDays() {
		return miDays;
	}

	public void setMiDays(String miDays) {
		this.miDays = miDays;
	}

	public String getMiHowCode() {
		return miHowCode;
	}

	public void setMiHowCode(String miHowCode) {
		this.miHowCode = miHowCode;
	}

	public String getMiDose() {
		return miDose;
	}

	public void setMiDose(String miDose) {
		this.miDose = miDose;
	}

	public Date getLastPullDate() {
		return lastPullDate;
	}

	public void setLastPullDate(Date lastPullDate) {
		this.lastPullDate = lastPullDate;
	}

	@Override
	public String toString() {
		return "ErpGoodsInfo{" +
			", orgId=" + orgId +
			", goodsId=" + goodsId +
			", goodsCode=" + goodsCode +
			", goodsName=" + goodsName +
			", logogram=" + logogram +
			", goodsSpec=" + goodsSpec +
			", manufacturer=" + manufacturer +
			", bzgg=" + bzgg +
			", barCode=" + barCode +
			", brandName=" + brandName +
			", chemName=" + chemName +
			", standCode=" + standCode +
			", unit=" + unit +
			", generalName=" + generalName +
			", lifeType=" + lifeType +
			", dayUnit=" + dayUnit +
			", inEffectDay=" + inEffectDay +
			", regMark=" + regMark +
			", regMarkValidity=" + regMarkValidity +
			", place=" + place +
			", goodsLm=" + goodsLm +
			", spflA=" + spflA +
			", spflB=" + spflB +
			", spflC=" + spflC +
			", spflD=" + spflD +
			", entrustName=" + entrustName +
			", entrustAddress=" + entrustAddress +
			", scdz=" + scdz +
			", newRepOrigin=" + newRepOrigin +
			", sanZz=" + sanZz +
			", swflCode=" + swflCode +
			", ckbs=" + ckbs +
			", dwbs=" + dwbs +
			", yyfl=" + yyfl +
			", yyflB=" + yyflB +
			", yyflC=" + yyflC +
			", yyflD=" + yyflD +
			", jgfl=" + jgfl +
			", csspfl=" + csspfl +
			", regDates=" + regDates +
			", kkYbmc=" + kkYbmc +
			", medInsCode=" + medInsCode +
			", medInsName=" + medInsName +
			", yblb=" + yblb +
			", purTaxP=" + purTaxP +
			", inTaxRate=" + inTaxRate +
			", purP=" + purP +
			", biddPrice=" + biddPrice +
			", saleTaxP=" + saleTaxP +
			", outTaxRate=" + outTaxRate +
			", saleP=" + saleP +
			", retailP=" + retailP +
			", maxRetailP=" + maxRetailP +
			", memPrice=" + memPrice +
			", creatorName=" + creatorName +
			", lastStfName=" + lastStfName +
			", lastRevTime=" + lastRevTime +
			", miDosageUnit=" + miDosageUnit +
			", miDosage=" + miDosage +
			", miConversion=" + miConversion +
			", miMinPackage=" + miMinPackage +
			", miDays=" + miDays +
			", miHowCode=" + miHowCode +
			", miDose=" + miDose +
			", lastPullDate=" + lastPullDate +
			"}";
	}
}
