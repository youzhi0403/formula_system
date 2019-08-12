package com.zakj.formula.dao.supplier.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.dao.supplier.ISupplierDao;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.DBHelper;

public class SupplierDaoImpl implements ISupplierDao {

	/**
	 * 插入数据
	 * 
	 * @param id 操作数据的主键， -1表示插入数据，其他值表示更新数据
	 * @param supplier 表的数据结构实体
	 * @throws SQLException
	 */
	public void add(SupplierBean supplier) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall(
				"{call insertOrReplaceSupplier(?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt, 
				null,
				supplier.getAddress(),
				supplier.getTelephone(),
				supplier.getContact(),
				supplier.getCompanyName());
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	/**
	 * 更新数据
	 * 
	 * @param supplier
	 * @throws SQLException
	 */
	public void update(SupplierBean supplier) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call insertOrReplaceSupplier(?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt, 
				supplier.getId(),
				supplier.getAddress(),
				supplier.getTelephone(),
				supplier.getContact(),
				supplier.getCompanyName());
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	/**
	 * 删除数据
	 * 
	 * @param id
	 *            需要删除的数据的id
	 * @return true 删除成功 false 删除失败
	 * @throws SQLException
	 */
	public void delete(Integer id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call deleteSupplier(?)}");
		cstmt.setObject(1, id);
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	/**
	 * 查询所有的提供商数据
	 * 
	 * @return 返回包含所有提供商数据实体对象的列表
	 * @throws SQLException
	 */
	public ArrayList<SupplierBean> findAll() throws SQLException {
		ArrayList<SupplierBean> list = new ArrayList<SupplierBean>();
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call selectAllSupplier()}");
		ResultSet resultSet = cstmt.executeQuery();
		SupplierBean supplier = null;
		while (resultSet.next()) {
			supplier = new SupplierBean();
			supplier.setId(resultSet.getInt("id"));
			supplier.setAddress(resultSet.getString("address"));
			supplier.setTelephone(resultSet.getString("telephone"));
			supplier.setContact(resultSet.getString("contact"));
			supplier.setCompanyName(resultSet.getString("companyName"));
			list.add(supplier);
		}
		C3P0Utils.close(cstmt, resultSet);
		return list;
	}

	@Override
	public PageBean<SupplierBean> likeName(SupplierBean bean, PageBean<SupplierBean> page) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call pro_SupplierPagination(?,?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt,
				bean.getAddress() == null?"":bean.getAddress(),
				(bean.getTelephone() == null || bean.getTelephone() == "")?null:bean.getTelephone(),
				bean.getContact() == null?"":bean.getContact(),
				bean.getCompanyName() == null?"":bean.getCompanyName(),
				page.getLimit(),
				page.getCurrentCount());
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<SupplierBean> list = new ArrayList<SupplierBean>();
		SupplierBean supplier = null;
		while (resultSet.next()) {
			supplier = new SupplierBean();
			supplier.setId(resultSet.getInt("id"));
			supplier.setAddress(resultSet.getString("address"));
			supplier.setContact(resultSet.getString("contact"));
			supplier.setTelephone(resultSet.getString("telephone"));
			supplier.setCompanyName(resultSet.getString("companyName"));
			list.add(supplier);
		}
		
		//查询供应商总数
		cstmt = conn.prepareCall("{call supplierTotalCount()}");
		resultSet = cstmt.executeQuery();
		if(resultSet.next()) {
			page.setTotalCount(resultSet.getInt(1));
		}

		page.setList(list);
		page.setTotalPage((int) Math.ceil(1.0*page.getTotalCount()/page.getCurrentCount()));
		C3P0Utils.close(cstmt, resultSet);
		return page;
	}

	@Override
	public SupplierBean find(Integer id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAll(List<SupplierBean> getsList) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		PreparedStatement cstmt = 
				conn.prepareStatement("insert Supplier(address, telephone, contact, companyName) values(?,?,?,?)");
		for (SupplierBean supplier : getsList) {
			DBHelper.prepareCall(cstmt, 
					supplier.getAddress(),
					supplier.getTelephone(),
					supplier.getContact(),
					supplier.getCompanyName());
			cstmt.addBatch();
		}
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public List<String> findAllSupplierName() throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		PreparedStatement cstmt = conn.prepareStatement("select companyName from Supplier");
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<String> supplierNames = new ArrayList<String>();
		while(resultSet.next()){
			supplierNames.add(resultSet.getString("companyName"));
		}
		C3P0Utils.close(cstmt, resultSet);
		return supplierNames;
	}
}
