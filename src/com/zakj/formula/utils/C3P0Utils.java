package com.zakj.formula.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Utils {
	private C3P0Utils() {
	};

	//数据源
	private static ComboPooledDataSource dataSource;
	//getLocal()拿到的是数据库的连接
	private static ThreadLocal<Connection> local;

	static {
		dataSource = new ComboPooledDataSource();
		local = new ThreadLocal<>();
	}

	/**
	 * 直接可以获取一个连接池
	 * 
	 * @return
	 */
	public static DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 获取与当前线程绑定的连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getCurrentConnection() throws SQLException {
		Connection conn = local.get();
		if (conn == null) {
			conn = getConnection();
			local.set(conn);
		}
		return conn;
	}

	/**
	 * 获取一条数据库连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
		Connection conn = dataSource.getConnection();
		System.out.println("获取数据库连接：" + conn);
		return conn;
	}

	/**
	 * 开启事务
	 * 
	 * @throws SQLException
	 */
	public static void beginTransation() throws SQLException {
		Connection conn = getCurrentConnection();
		if (conn != null) {
			conn.setAutoCommit(false);
		}
	}

	/**
	 * 提交事务
	 * 
	 * @throws SQLException
	 */
	public static void commit() throws SQLException {
		Connection conn = getCurrentConnection();
		if (conn != null) {
			conn.commit();
		}
	}

	/**
	 * 事务回滚
	 * @throws SQLException
	 */
	public static void rollback(){
		Connection conn;
		try {
			conn = getCurrentConnection();
			if (conn != null) {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提交并释放connection资源
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	public static void commitAndRelease(){
		Connection conn;
		try {
			conn = getCurrentConnection();
			if (conn != null) {
				conn.commit(); // 事务提交
				conn.close();// 关闭资源
				local.remove();// 从线程绑定中移除
				System.out.println("关闭数据库连接：" + conn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭资源
	 * @param cstmt
	 * @param result
	 * @throws SQLException
	 */
	public static void close(PreparedStatement cstmt, ResultSet result) throws SQLException{
		if (result != null) {
			result.close();
		}
		if (cstmt != null) {
			cstmt.close();
		}
	}
}
