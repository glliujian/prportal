package com.jeeplus.modules.sys.entity;

import java.util.List;

public class BasePortalPermissionMenu {
	private String menuid;
	private String parentid;
	private String name;
	private String platform;
	private String type;
	private String url;
	private String target;
	private String icon;
	private String sort;
	
	private List<BasePortalPermissionMenu> subMenus;
	
	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	public List<BasePortalPermissionMenu> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<BasePortalPermissionMenu> subMenus) {
		this.subMenus = subMenus;
	}
}
