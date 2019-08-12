package com.zakj.formula.dao.role;

import java.sql.SQLException;
import java.util.List;

import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.dao.IBaseDao;

public interface IRoleDao extends IBaseDao<RoleBean>{
	public List<RoleBean> findRoleListByUserId(Integer id) throws SQLException;
	
	public List<RoleBean> findRoleList(RoleBean role) throws SQLException;

	public void saveRolePrivileges(RoleBean role) throws SQLException;

}
