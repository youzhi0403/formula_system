package com.zakj.formula.dao.user;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.sun.istack.internal.NotNull;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.bean.UserBean;
import com.zakj.formula.dao.IBaseDao;

public interface IUserDao extends IBaseDao<UserBean>{
	public UserBean find(String account) throws SQLException;
	
	public List<UserBean> findUserList(@NotNull UserBean t) throws SQLException;
	
	public List<String> findPrivilegeCodeByUID(Integer uid) throws SQLException;

	public Set<RoleBean> findRolesByUid(Integer id) throws SQLException;

	public Set<PrivilegeBean> findPrivilegeByRid(int id) throws SQLException;

	public List<String> findAllPrivilegeUrils() throws SQLException;
}
