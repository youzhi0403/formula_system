package com.zakj.formula.bean;

import java.util.Set;

public class PrivilegeBean {

	private int id; //id
	private String name;//权限名称
	private String desc;//权限说明
	private String url; //权限路径
	private Integer zindex;
	private PrivilegeBean parentPrivilege; //上级权限
	private Set<PrivilegeBean> childrenPrivilege; //下级权限集合
	
	public PrivilegeBean() {
		super();
	}
	public PrivilegeBean(int id, String name, String desc, String url,
			Integer zindex) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.url = url;
		this.zindex = zindex;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public PrivilegeBean getParentPrivilege() {
		return parentPrivilege;
	}
	public void setParentPrivilege(PrivilegeBean parentPrivilege) {
		this.parentPrivilege = parentPrivilege;
	}
	public Set<PrivilegeBean> getChildrenPrivilege() {
		return childrenPrivilege;
	}
	public void setChildrenPrivilege(Set<PrivilegeBean> childrenPrivilege) {
		this.childrenPrivilege = childrenPrivilege;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getZindex() {
		return zindex;
	}
	public void setZindex(Integer zindex) {
		this.zindex = zindex;
	}
	@Override
	public String toString() {
		return "PrivilegeBean [id=" + id + ", name=" + name + ", desc=" + desc
				+ ", parentPrivilege=" + parentPrivilege + "]";
	}
	
}
