package com.zakj.formula.service;

import java.util.List;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.exception.CustomException;

public interface IPrivilegeService {
	
	/*public boolean addPrivilege(PrivilegeBean privilege) throws CustomException;
	
	public boolean deletePrivilege(Integer id) throws CustomException;
	
	public boolean updatePrivilege(PrivilegeBean privilege) throws CustomException;*/
	
	public PageBean<PrivilegeBean> showPrivilegeList(int currentPage, 
		int currentCount, PrivilegeBean privilege) throws CustomException;
}
