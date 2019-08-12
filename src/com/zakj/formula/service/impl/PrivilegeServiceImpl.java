package com.zakj.formula.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.dao.privilege.IPrivilegeDao;
import com.zakj.formula.dao.privilege.impl.PrivilegeDaoImpl;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IPrivilegeService;
import com.zakj.formula.utils.C3P0Utils;

public class PrivilegeServiceImpl implements IPrivilegeService {

	private IPrivilegeDao dao;
	
	public PrivilegeServiceImpl(){
		dao = new PrivilegeDaoImpl();
	}

	/*@Override
	public boolean addPrivilege(PrivilegeBean privilege) throws CustomException {
		try {
			return dao.add(privilege);
		} catch (SQLException e) {
			System.out.println("ErrorCode:"+e.getErrorCode()+ "  错误信息：" + e.getMessage());
			throw new CustomException(CustomException.SQL_EXCEPTION);
		}
	}

	@Override
	public boolean deletePrivilege(Integer id) throws CustomException {
		try {
			return dao.delete(id);
		} catch (SQLException e) {
			System.out.println("ErrorCode:"+e.getErrorCode()+ "  错误信息：" + e.getMessage());
			throw new CustomException(CustomException.SQL_EXCEPTION);
		}
	}

	@Override
	public boolean updatePrivilege(PrivilegeBean privilege)
			throws CustomException {
		try {
			return dao.update(privilege);
		} catch (SQLException e) {
			System.out.println("ErrorCode:"+e.getErrorCode()+ "  错误信息：" + e.getMessage());
			throw new CustomException(CustomException.SQL_EXCEPTION);
		}
	}*/

	@Override
	public PageBean<PrivilegeBean> showPrivilegeList(int currentPage,
			int currentCount, PrivilegeBean privilege) throws CustomException {
		PageBean<PrivilegeBean> pageBean = new PageBean<PrivilegeBean>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setCurrentCount(currentCount);
		try {
			C3P0Utils.beginTransation();
			pageBean = dao.findPrivilegeList(privilege, pageBean);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
		return pageBean;
	}
	

}
