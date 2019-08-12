package com.zakj.formula.test;

import java.sql.SQLException;

import org.junit.Test;

import com.zakj.formula.bean.UserBean;
import com.zakj.formula.dao.user.impl.UserDaoImpl;


public class LoginStoredProdureTest {
	
	@Test
	public void login() throws SQLException{
		UserDaoImpl dao = new UserDaoImpl();
		UserBean bean = dao.find("123");
		System.out.println(bean);
	}
	
}
