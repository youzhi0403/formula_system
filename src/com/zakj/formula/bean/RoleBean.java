package com.zakj.formula.bean;

import java.util.Set;

public class RoleBean {
	
	private int id; //主键
	private String name;//角色名称
	private String desc;//角色说明
	private Set<PrivilegeBean> privileges;//角色拥有的权限
	
	public RoleBean() {
		super();
	}
	public RoleBean(int id, String name, String desc,
			Set<PrivilegeBean> privileges) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.privileges = privileges;
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
	public Set<PrivilegeBean> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(Set<PrivilegeBean> privileges) {
		this.privileges = privileges;
	}
	@Override
	public String toString() {
		return "RolseBean [id=" + id + ", name=" + name + ", desc=" + desc
				+ ", privileges=" + privileges + "]";
	}
	
}
