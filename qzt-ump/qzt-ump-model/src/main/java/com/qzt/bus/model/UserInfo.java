package com.qzt.bus.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 员工表
 * </p>
 *
 * @author snow
 * @since 2023-08-29
 */
@TableName("user_info")
public class UserInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

	@TableId(value="uid", type= IdType.AUTO)
	private Integer uid;
    /**
     * 员工姓名
     */
	@TableField("u_name")
	private String uName;
    /**
     * 小程序登录密码
     */
	@TableField("u_password")
	private String uPassword;
    /**
     * 0正式员工 1试用期员工 -1 离职员工
     */
	@TableField("u_status")
	private Integer uStatus;
    /**
     * 电子邮箱
     */
	@TableField("u_email")
	private String uEmail;
    /**
     * 电话
     */
	@TableField("u_phone")
	private String uPhone;
    /**
     * 小程序unionid
     */
	@TableField("u_unionid")
	private String uUnionid;
    /**
     * 药镜小程序openid
     */
	@TableField("u_openid")
	private String uOpenid;
    /**
     * 特药在线公众号openid
     */
	@TableField("u_official_openid")
	private String uOfficialOpenid;
    /**
     * 头像地址
     */
	@TableField("u_picture")
	private String uPicture;
    /**
     * 创建时间
     */
	@TableField("u_timestamp")
	private Date uTimestamp;
    /**
     * 药房 pharmacy_list.pid
     */
	@TableField("u_pharmacy")
	private Integer uPharmacy;
    /**
     * 职位 position_list.pid
     */
	@TableField("u_position")
	private Integer uPosition;
    /**
     * 组ID group_list.gid （只有组管理员此字段有用）
     */
	@TableField("u_group")
	private Integer uGroup;
    /**
     * 部门ID
     */
	@TableField("u_division")
	private Integer uDivision;
    /**
     * 所属药企 用来给药企管理员录入试题时使用 其他人员为空即可 manufacturer_list.ml_id
     */
	@TableField("u_company")
	private Integer uCompany;
    /**
     * 操作员
     */
	@TableField("u_operator")
	private Integer uOperator;
    /**
     * 更新时间
     */
	@TableField("u_update_time")
	private Date uUpdateTime;
    /**
     * 员工推荐码
     */
	@TableField("u_share_code")
	private Integer uShareCode;
	/**
	 * 部门编号
	 */
	@TableField("pid")
	private Integer pid;
	/**
	 * 部门名称
	 */
	@TableField("ph_short_name")
	private String phShortName;

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getPhShortName() {
		return phShortName;
	}

	public void setPhShortName(String phShortName) {
		this.phShortName = phShortName;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getuPassword() {
		return uPassword;
	}

	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}

	public Integer getuStatus() {
		return uStatus;
	}

	public void setuStatus(Integer uStatus) {
		this.uStatus = uStatus;
	}

	public String getuEmail() {
		return uEmail;
	}

	public void setuEmail(String uEmail) {
		this.uEmail = uEmail;
	}

	public String getuPhone() {
		return uPhone;
	}

	public void setuPhone(String uPhone) {
		this.uPhone = uPhone;
	}

	public String getuUnionid() {
		return uUnionid;
	}

	public void setuUnionid(String uUnionid) {
		this.uUnionid = uUnionid;
	}

	public String getuOpenid() {
		return uOpenid;
	}

	public void setuOpenid(String uOpenid) {
		this.uOpenid = uOpenid;
	}

	public String getuOfficialOpenid() {
		return uOfficialOpenid;
	}

	public void setuOfficialOpenid(String uOfficialOpenid) {
		this.uOfficialOpenid = uOfficialOpenid;
	}

	public String getuPicture() {
		return uPicture;
	}

	public void setuPicture(String uPicture) {
		this.uPicture = uPicture;
	}

	public Date getuTimestamp() {
		return uTimestamp;
	}

	public void setuTimestamp(Date uTimestamp) {
		this.uTimestamp = uTimestamp;
	}

	public Integer getuPharmacy() {
		return uPharmacy;
	}

	public void setuPharmacy(Integer uPharmacy) {
		this.uPharmacy = uPharmacy;
	}

	public Integer getuPosition() {
		return uPosition;
	}

	public void setuPosition(Integer uPosition) {
		this.uPosition = uPosition;
	}

	public Integer getuGroup() {
		return uGroup;
	}

	public void setuGroup(Integer uGroup) {
		this.uGroup = uGroup;
	}

	public Integer getuDivision() {
		return uDivision;
	}

	public void setuDivision(Integer uDivision) {
		this.uDivision = uDivision;
	}

	public Integer getuCompany() {
		return uCompany;
	}

	public void setuCompany(Integer uCompany) {
		this.uCompany = uCompany;
	}

	public Integer getuOperator() {
		return uOperator;
	}

	public void setuOperator(Integer uOperator) {
		this.uOperator = uOperator;
	}

	public Date getuUpdateTime() {
		return uUpdateTime;
	}

	public void setuUpdateTime(Date uUpdateTime) {
		this.uUpdateTime = uUpdateTime;
	}

	public Integer getuShareCode() {
		return uShareCode;
	}

	public void setuShareCode(Integer uShareCode) {
		this.uShareCode = uShareCode;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
			", uid=" + uid +
			", uName=" + uName +
			", uPassword=" + uPassword +
			", uStatus=" + uStatus +
			", uEmail=" + uEmail +
			", uPhone=" + uPhone +
			", uUnionid=" + uUnionid +
			", uOpenid=" + uOpenid +
			", uOfficialOpenid=" + uOfficialOpenid +
			", uPicture=" + uPicture +
			", uTimestamp=" + uTimestamp +
			", uPharmacy=" + uPharmacy +
			", uPosition=" + uPosition +
			", uGroup=" + uGroup +
			", uDivision=" + uDivision +
			", uCompany=" + uCompany +
			", uOperator=" + uOperator +
			", uUpdateTime=" + uUpdateTime +
			", uShareCode=" + uShareCode +
			", pid=" + pid +
			", phShortName=" + phShortName +
			"}";
	}
}
