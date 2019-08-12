package com.zakj.formula.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.exception.CustomException;

public interface IRoleService {
	public void addRole(RoleBean role) throws CustomException;
	
	public void deleteRole(Integer id) throws CustomException;
	
	public void updateRole(RoleBean role) throws CustomException;
	
	public PageBean<RoleBean> showRoleList(int currentPage, int currentCount, RoleBean role) throws CustomException;

	public ArrayList<Map<String, Object>> showPrivilegeZtreeList(Integer rid) throws CustomException;

	public List<RoleBean> showRoleNameList() throws CustomException;

	public void savePrivilege(RoleBean role) throws CustomException;
}
