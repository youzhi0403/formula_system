package com.zakj.formula.dao.privilege;

import java.sql.SQLException;
import java.util.List;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.dao.IBaseDao;

public interface IPrivilegeDao{
	public PageBean<PrivilegeBean> findPrivilegeList(PrivilegeBean privilege, PageBean<PrivilegeBean> page) throws SQLException;
	
	public List<PrivilegeBean> findPrivilegeListByRid(Integer rid) throws SQLException;
	
	public List<PrivilegeBean> findAllPrilegeList() throws SQLException;
}
