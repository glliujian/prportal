/**
 * MFG
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 国际化语言设置Entity
 * @author Jack
 * @version 2017-08-29
 */
public class SysSimpleLanguage extends DataEntity<SysSimpleLanguage> {
	
	private static final long serialVersionUID = 1L;
	private String code;		// 简体中文
	private String cn;		// 简体中文
	private String hk;		// 繁体中文
	private String en;		// 英文
	private String vn;		// 越南文
	private String cam;		// 柬埔寨
	private String isSaveToPage;		// 前端框架国际化
	
	public SysSimpleLanguage() {
		super();
	}

	public SysSimpleLanguage(String id){
		super(id);
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@ExcelField(title="简体中文", align=2, sort=7)
	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}
	
	@ExcelField(title="繁体中文", align=2, sort=8)
	public String getHk() {
		return hk;
	}

	public void setHk(String hk) {
		this.hk = hk;
	}
	
	@ExcelField(title="英文", align=2, sort=9)
	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}
	
	@ExcelField(title="越南文", align=2, sort=10)
	public String getVn() {
		return vn;
	}

	public void setVn(String vn) {
		this.vn = vn;
	}
	
	@ExcelField(title="柬埔寨", align=2, sort=11)
	public String getCam() {
		return cam;
	}

	public void setCam(String cam) {
		this.cam = cam;
	}
	
	@ExcelField(title="前端框架国际化", dictType="yes_no", align=2, sort=12)
	public String getIsSaveToPage() {
		return isSaveToPage;
	}

	public void setIsSaveToPage(String isSaveToPage) {
		this.isSaveToPage = isSaveToPage;
	}
	
}