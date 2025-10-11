package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@TableName("dgm_qzt_base_bank")
public class QztBaseBank extends BaseModel {

    private static final long serialVersionUID = 1L;

	@TableField("bank_name")
	private String bankName;
	@TableField("s_pic")
	private String sPic;
	@TableField("b_pic")
	private String bPic;


	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getsPic() {
		return sPic;
	}

	public void setsPic(String sPic) {
		this.sPic = sPic;
	}

	public String getbPic() {
		return bPic;
	}

	public void setbPic(String bPic) {
		this.bPic = bPic;
	}

	@Override
	public String toString() {
		return "QztBaseBank{" +
			", bankName=" + bankName +
			", sPic=" + sPic +
			", bPic=" + bPic +
			"}";
	}
}
