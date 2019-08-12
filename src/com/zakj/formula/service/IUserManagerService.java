package com.zakj.formula.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.bean.UserBean;
import com.zakj.formula.exception.CustomException;

public interface IUserManagerService {

	public void addUser(UserBean user) throws CustomException;
	
	public void deleteUser(Integer id) throws CustomException;
	
	public void updateUser(UserBean user) throws CustomException;
	
	public PageBean<UserBean> showUserList(int currentPage, int currentCount, UserBean user) throws CustomException;

	public List<String> findPrivilegeUrlByUID(Integer uid) throws CustomException;

}
