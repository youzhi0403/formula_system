package com.zakj.formula.bean;

import java.util.List;
import java.util.Set;

public class UserBean {
	private Integer id;  //主键
	private String name;  //名字
	private String account; //账号
	private String password;  //密码
	private Set<RoleBean> roles; //角色集合
	
	private List<String> urls;
	
	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<RoleBean> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleBean> roles) {
		this.roles = roles;
	}

	/**
	 * 验证权限是否存在
	 */
	public boolean checkPrivilegeByName(String name){
		if(isAdmin()){
			return true;
		}
		System.out.println("验证当前登录用户是否有权限：" + name);
		for(RoleBean r : roles){
			for(PrivilegeBean p : r.getPrivileges()){
				if(name.equals(p.getName())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 验证权限对应的url是否存在
	 */
	public boolean checkPrivilegeByUrl(String url) {
		if(isAdmin()){
			return true;
		}
		//判断当前的url是否存在urls中
		if(urls.contains(url)){
			for(RoleBean r : roles){
				for(PrivilegeBean p : r.getPrivileges()){
					if(url.equals(p.getUrl())){
						return true;
					}
				}
			}
		} else {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断当前登录用户是否是超级管理员
	 */
	public boolean isAdmin(){
		return "admin".equals(name);
	}
	
	@Override
	public String toString() {
		return "UserBean [id=" + id + ", name=" + name + ", account=" + account
				+ ", password=" + password + ", rolse=" + roles + "]";
	}
}
