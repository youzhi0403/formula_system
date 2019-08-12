package com.zakj.formula.test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.bean.UserBean;
import com.zakj.formula.dao.user.impl.UserDaoImpl;
import com.zakj.formula.exception.CustomException;

public class UserTest {

	@Test
	public void insertUser() throws SQLException, CustomException{
		UserDaoImpl dao = new UserDaoImpl();
		UserBean user = new UserBean();
		user.setAccount("huang");
		user.setPassword("123");
		user.setName("huang");
		RoleBean role = new RoleBean();
		role.setId(1);
		Set<RoleBean> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);
		dao.add(user);
	}
	
}
