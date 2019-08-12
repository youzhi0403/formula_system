package com.zakj.formula.dao.user.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.sun.istack.internal.NotNull;
import com.sun.org.apache.regexp.internal.recompile;
import com.sun.xml.internal.ws.org.objectweb.asm.Type;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.bean.UserBean;
import com.zakj.formula.dao.user.IUserDao;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.DBHelper;

public class UserDaoImpl implements IUserDao {

	// 查找账号
	@Override
	public UserBean find(String account) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call SelectUserTable(?)}");
		cstmt.setString(1, account);
		ResultSet resultSet = cstmt.executeQuery();
		UserBean userBean = null;
		if (resultSet.next()) {
			userBean = new UserBean();
			userBean.setId(resultSet.getInt(1));
			userBean.setAccount(resultSet.getString(2));
			userBean.setPassword(resultSet.getString(3));
			userBean.setName(resultSet.getString(4));
		}
		C3P0Utils.close(cstmt, resultSet);
		System.out.println(userBean);
		return userBean;
	}

	// 添加账号
	@Override
	public void add(UserBean t) throws SQLException, CustomException {
		Connection conn = C3P0Utils.getCurrentConnection();
		conn.setAutoCommit(false);

		CallableStatement cstmt = conn
				.prepareCall("{call insertOrReplaceUserTable(?,?,?,?)}");
		cstmt.setObject(1, null);
		cstmt.setObject(2, t.getAccount());
		cstmt.setObject(3, t.getPassword());
		cstmt.setObject(4, t.getName());
		cstmt.registerOutParameter(1, Type.INT);
		cstmt.executeUpdate();
		// 获取新增的用户主键id
		int uid = cstmt.getInt(1);
		cstmt = conn.prepareCall("{call insertuser_roles(?,?)}");
		for (RoleBean role : t.getRoles()) {
			cstmt.setObject(1, uid);
			cstmt.setObject(2, role.getId());
			cstmt.addBatch();
		}
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, null);
	}

	// 更新账号
	@Override
	public void update(UserBean t) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call insertOrReplaceUserTable(?,?,?,?)}");
		cstmt.setObject(1, t.getId());
		cstmt.setObject(2, t.getAccount());
		cstmt.setObject(3, t.getPassword());
		cstmt.setObject(4, t.getName());
		cstmt.registerOutParameter(1, Type.INT);
		cstmt.executeUpdate();

		// 删除用户的所有角色
		cstmt = conn.prepareCall("{call deleteuser_roles(?)}");
		cstmt.setObject(1, t.getId());
		cstmt.executeUpdate();

		// 添加更新后的用户角色
		cstmt = conn.prepareCall("{call insertuser_roles(?,?)}");
		for (RoleBean role : t.getRoles()) {
			cstmt.setObject(1, t.getId());
			cstmt.setObject(2, role.getId());
			cstmt.addBatch();
		}
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public UserBean find(Integer id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	// 删除账号
	@Override
	public void delete(@NotNull Integer id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call deleteUser(?)}");
		cstmt.setObject(1, id);
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public List<UserBean> findUserList(@NotNull UserBean t) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call pro_UserTable(?,?)}");
		cstmt.setObject(1, t.getAccount());
		cstmt.setObject(2, t.getName());
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<UserBean> list = new ArrayList<UserBean>();
		UserBean user = null;
		while (resultSet.next()) {
			user = new UserBean();
			user.setId(resultSet.getInt(1));
			user.setAccount(resultSet.getString(2));
			user.setPassword(resultSet.getString(3));
			user.setName(resultSet.getString(4));
			list.add(user);
		}
		C3P0Utils.close(null, resultSet);

		cstmt = conn.prepareCall("{call selectRolesByUID(?)}");
		RoleBean role = null;
		Set<RoleBean> set = null;
		ResultSet resultSet2 = null;
		for (UserBean userBean : list) {
			cstmt.setObject(1, userBean.getId());
			resultSet2 = cstmt.executeQuery();
			set = new HashSet<RoleBean>();
			while (resultSet2.next()) {
				role = new RoleBean();
				role.setId(resultSet2.getInt(1));
				role.setName(resultSet2.getString(2));
				role.setDesc(resultSet2.getString(3));
				set.add(role);
			}
			resultSet2.close();
			userBean.setRoles(set);
			set = null;// 置为null，防止set设置给下一个uerbean对象
		}
		C3P0Utils.close(cstmt, resultSet2);
		return list;
	}

	@Override
	public List<String> findPrivilegeCodeByUID(Integer uid) throws SQLException {
		
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call selectPrivilegeCodeByUID(?)}");
		cstmt.setObject(1, uid);
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<String> list = new ArrayList<String>();
		while (resultSet.next()) {
			list.add(resultSet.getString("code"));
		}
		C3P0Utils.close(cstmt, resultSet);
		return list;
	
	}

	@Override
	public Set<RoleBean> findRolesByUid(Integer id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call selectRolesByUID(?)}");
		DBHelper.prepareCall(cstmt, id);
		ResultSet resultSet = cstmt.executeQuery();
		Set<RoleBean> roles = new HashSet<RoleBean>();
		RoleBean roleBean = null;
		while (resultSet.next()) {
			roleBean = new RoleBean();
			roleBean.setId(resultSet.getInt("id"));
			roleBean.setDesc(resultSet.getString("role_desc"));
			roleBean.setName(resultSet.getString("name"));
			roles.add(roleBean);
		}
		return roles;
	}

	@Override
	public Set<PrivilegeBean> findPrivilegeByRid(int id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call selectPrivilegeByRid(?)}");
		DBHelper.prepareCall(cstmt, id);
		ResultSet resultSet = cstmt.executeQuery();
		Set<PrivilegeBean> privileges = new HashSet<PrivilegeBean>();
		PrivilegeBean privilege = null;
		while (resultSet.next()) {
			privilege = new PrivilegeBean();
			privilege.setId(resultSet.getInt("id"));
			privilege.setDesc(resultSet.getString("desc"));
			privilege.setName(resultSet.getString("name"));
			privilege.setUrl(resultSet.getString("url"));
			privileges.add(privilege);
		}
		return privileges;
	}

	@Override
	public List<String> findAllPrivilegeUrils() throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call selectAllPrivilege()}");
		ResultSet resultSet = cstmt.executeQuery();
		List<String> urls = new ArrayList<String>();
		while (resultSet.next()) {
			urls.add(resultSet.getString("url"));
		}
		return urls;
	}
}
