package com.zakj.formula.dao.supplier;

import java.sql.SQLException;
import java.util.List;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.dao.IBaseDao;

public interface ISupplierDao extends IBaseDao<SupplierBean>{
	public PageBean<SupplierBean> likeName(SupplierBean bean, PageBean<SupplierBean> page) throws SQLException;
	public List<SupplierBean> findAll() throws SQLException;
	public void addAll(List<SupplierBean> getsList) throws SQLException;
	public List<String> findAllSupplierName() throws SQLException;
}
