package com.zakj.formula.dao.privilege.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.dao.privilege.IPrivilegeDao;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.DBHelper;

public class PrivilegeDaoImpl implements IPrivilegeDao {

	@Override
	public PageBean<PrivilegeBean> findPrivilegeList(PrivilegeBean privilege, PageBean<PrivilegeBean> page) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call pro_PrivilegePagination(?,?,?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt, 
				privilege.getId(),
				privilege.getName(),
				privilege.getDesc(),
				privilege.getUrl(),
				page.getCurrentCount(),
				page.getCurrentPage());
		cstmt.registerOutParameter(7, Types.INTEGER);
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<PrivilegeBean> list = new ArrayList<PrivilegeBean>();
		PrivilegeBean privilege_tmp = null;
		while (resultSet.next()) {
			privilege_tmp = new PrivilegeBean();
			privilege_tmp.setId(resultSet.getInt(1));
			privilege_tmp.setName(resultSet.getString(2));
			privilege_tmp.setDesc(resultSet.getString(3));
			privilege_tmp.setUrl(resultSet.getString(4));
			privilege_tmp.setZindex(resultSet.getInt(5));
			list.add(privilege_tmp);
		}
		page.setTotalCount(resultSet.getInt(7));
		page.setList(list);
		page.setTotalPage((int) Math.ceil(1.0*page.getTotalCount()/page.getCurrentCount()));
		C3P0Utils.close(cstmt, resultSet);
		return page;
	}

	@Override
	public List<PrivilegeBean> findPrivilegeListByRid(Integer rid)
			throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call selectPrivilegeByRid(?)}");
		cstmt.setObject(1, rid);
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<PrivilegeBean> list = new ArrayList<PrivilegeBean>();
		PrivilegeBean privilege = null;
		while (resultSet.next()) {
			privilege = new PrivilegeBean();
			privilege.setId(resultSet.getInt("id"));
			privilege.setName(resultSet.getString("name"));
			privilege.setZindex(resultSet.getInt("parentid"));
			list.add(privilege);
		}
		C3P0Utils.close(cstmt, resultSet);
		return list;
	}

	@Override
	public List<PrivilegeBean> findAllPrilegeList() throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call selectAllPrivilege()}");
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<PrivilegeBean> list = new ArrayList<PrivilegeBean>();
		PrivilegeBean privilege = null;
		while (resultSet.next()) {
			privilege = new PrivilegeBean();
			privilege.setId(resultSet.getInt("id"));
			privilege.setName(resultSet.getString("name"));
			privilege.setZindex(resultSet.getInt("parentid"));
			list.add(privilege);
		}
		return list;
	}
}
