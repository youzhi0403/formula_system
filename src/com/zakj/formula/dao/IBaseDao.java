package com.zakj.formula.dao;

import java.sql.SQLException;

import com.zakj.formula.exception.CustomException;

public interface IBaseDao<T> {
	public void add(T t) throws SQLException, CustomException;
	public void update(T t) throws SQLException;
	public T find(Integer id) throws SQLException;
	public void delete(Integer id) throws SQLException;
}
