package com.zakj.formula.dao.role.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.sun.xml.internal.ws.org.objectweb.asm.Type;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.dao.role.IRoleDao;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.utils.C3P0Utils;

public class RoleDaoImpl implements IRoleDao {

	@Override
	public void add(RoleBean t) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call insertOrReplaceRoles(?,?,?)}");
		cstmt.setObject(1, null);
		cstmt.setObject(2, t.getName());
		cstmt.setObject(3, t.getDesc());
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public void update(RoleBean t) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call insertOrReplaceRoles(?,?,?)}");
		cstmt.setObject(1, t.getId());
		cstmt.setObject(2, t.getName());
		cstmt.setObject(3, t.getDesc());
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public RoleBean find(Integer id) throws SQLException {
		return null;
	}

	@Override
	public void delete(Integer id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call deleteRoles_privilegerolse(?)}");
		cstmt.setObject(1, id);
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	// 根据账号id查找账号的角色
	@Override
	public List<RoleBean> findRoleListByUserId(Integer id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call select_RU(?)}");
		cstmt.setObject(1, id);
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<RoleBean> list = new ArrayList<RoleBean>();
		RoleBean role = null;
		while (resultSet.next()) {
			role = new RoleBean();
			role.setId(resultSet.getInt(1));
			role.setName(resultSet.getString(2));
			role.setDesc(resultSet.getString(3));
			list.add(role);
		}
		C3P0Utils.close(cstmt, resultSet);
		return list;
	}

	@Override
	public List<RoleBean> findRoleList(RoleBean role) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call pro_Roles(?)}");
		cstmt.setObject(1, role.getName()==null?"":role.getName());
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<RoleBean> list = new ArrayList<RoleBean>();
		RoleBean role_tmp = null;
		while (resultSet.next()) {
			role_tmp = new RoleBean();
			role_tmp.setId(resultSet.getInt(1));
			role_tmp.setName(resultSet.getString(2));
			role_tmp.setDesc(resultSet.getString(3));
			list.add(role_tmp);
		}
		C3P0Utils.close(cstmt, resultSet);
		return list;
	}

	@Override
	public void saveRolePrivileges(RoleBean role) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt;
		// 删除角色的权限
		cstmt = conn.prepareCall("{call deleteprivilege_rolse(?)}");
		cstmt.setObject(1, role.getId());
		cstmt.executeUpdate();

		// 插入角色新的权限
		cstmt = conn.prepareCall("{call insertprivilege_rolse(?,?)}");
		Set<PrivilegeBean> privileges = role.getPrivileges();
		for (PrivilegeBean privilegeBean : privileges) {
			cstmt.setObject(1, privilegeBean.getId());
			cstmt.setObject(2, role.getId());
			cstmt.addBatch();
		}
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, null);
	}
}
