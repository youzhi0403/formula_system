package com.zakj.formula.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.tomcat.jni.User;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.bean.UserBean;
import com.zakj.formula.dao.user.IUserDao;
import com.zakj.formula.dao.user.impl.UserDaoImpl;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IUserManagerService;
import com.zakj.formula.utils.C3P0Utils;

public class UserManagerServiceImpl implements IUserManagerService {

	private IUserDao dao;
	
	public UserManagerServiceImpl(){
		dao = new UserDaoImpl();
	}
	
	@Override
	public void addUser(UserBean user) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.add(user);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_ADD);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void deleteUser(Integer id) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.delete(id);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_DELETE);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void updateUser(UserBean user) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.update(user);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_UPDATE);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public PageBean<UserBean> showUserList(int currentPage, int currentCount,
			UserBean user) throws CustomException {
		PageBean<UserBean> pageBean = new PageBean<UserBean>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setCurrentCount(currentCount);
		List<UserBean> list = null;
		try {
			C3P0Utils.beginTransation();
			list = dao.findUserList(user);
			
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
		int totalCount = list.size();
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		pageBean.setTotalCount(totalCount);
		int start = (currentPage-1)*currentCount;
		if(start > totalCount){
			currentPage = totalPage;
			start = currentPage!=0?((currentPage-1)*currentCount):0;
		}
		int end = currentPage*currentCount;
		end = end > totalCount? totalCount : end;
		pageBean.setList(list.subList(start, end));
		return pageBean;
	}
	
	@Override
	public List<String> findPrivilegeUrlByUID(Integer uid) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			return dao.findPrivilegeCodeByUID(uid);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

}
