package com.zakj.formula.test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.dao.role.impl.RoleDaoImpl;

public class RoleTest {

	@Test
	public void updateRole() throws SQLException{
		RoleDaoImpl dao = new RoleDaoImpl();
		PrivilegeBean privilege = new PrivilegeBean(2,"aa","ss","url",3);
		Set<PrivilegeBean> set = new HashSet<PrivilegeBean>();
		set.add(privilege);
		RoleBean role = new RoleBean(1,"huang","dd",set);
		dao.update(role);
	}
	
}
