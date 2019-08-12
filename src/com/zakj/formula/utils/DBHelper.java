package com.zakj.formula.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {
	
	public static void prepareCall(PreparedStatement cstmt,Object... object) throws SQLException{
		for (int i = 1; i <= object.length; i++) {
			cstmt.setObject(i, object[i-1]);
		}
	}
	
//	public <T> T toBean(Class<T> clazz, ResultSet resultSet) throws InstantiationException, IllegalAccessException{
//		T t = clazz.newInstance();
//		clazz.getMethods();
//		t.
//	}
}
