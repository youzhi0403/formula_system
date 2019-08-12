package com.zakj.formula.dao.material.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.dao.material.IMaterialDao;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.DBHelper;

public class MaterialDaoImpl implements IMaterialDao {
	@Override
	public void add(MaterialBean material) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call insertOrReplaceMaterial(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt,
				null,
				material.getChineseName(),
				material.getName(),
				material.getOrigin(),
				material.getMApparentState(),
				material.getSid(),
				material.getCode(),
				material.getInci_en(),
				material.getInci_cn(),
				material.getApplication(),
				material.getPackingWay(),
				material.getPrice(),
				material.getUnit());
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public void update(MaterialBean t) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call insertOrReplaceMaterial(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt, 
				t.getId(),
				t.getChineseName(),
				t.getName(),
				t.getOrigin(),
				t.getMApparentState(),
				t.getSid(),
				t.getCode(),
				t.getInci_en(),
				t.getInci_cn(),
				t.getApplication(),
				t.getPackingWay(),
				t.getPrice(),
				t.getUnit());
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public void delete(Integer id) throws SQLException {
		 Connection conn = C3P0Utils.getCurrentConnection();
		 CallableStatement cstmt = conn.prepareCall("{call deleteMaterial(?)}");
		 DBHelper.prepareCall(cstmt, id);
		 cstmt.executeUpdate();
		 C3P0Utils.close(cstmt, null);
	}

	@Override
	public MaterialBean find(Integer id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call selectMaterial(?)}");
		DBHelper.prepareCall(cstmt, id);
		ResultSet resultSet = cstmt.executeQuery();
		if (resultSet.next()) {
			MaterialBean material = new MaterialBean();
			material.setId(resultSet.getInt("id"));
			material.setName(resultSet.getString("name"));
			material.setOrigin(resultSet.getString("origin"));
			material.setMApparentState(resultSet.getString("mApparentState"));
			material.setSid(resultSet.getInt("supplierId"));
			material.setPrice(resultSet.getBigDecimal("price"));
			material.setUnit(resultSet.getString("unit"));
			material.setCode(resultSet.getString("code"));
			material.setInci_cn(resultSet.getString("inci_cn"));
			material.setInci_en(resultSet.getString("inci_en"));
			material.setApplication(resultSet.getString("application"));
			material.setPackingWay(resultSet.getString("packingWay"));
			material.setChineseName(resultSet.getString("chineseName"));
			return material;
		}
		return null;
	}
	
	@Override
	public PageBean<MaterialBean> findMaterialList(MaterialBean material, PageBean<MaterialBean> page)throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call pro_M_SPagination(?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt,
				material.getName()==null?"":material.getName(),
				material.getCode()==null?"":material.getCode(),
				material.getInci_cn()==null?"":material.getInci_cn(),
				page.getLimit(),
				page.getCurrentCount());
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<MaterialBean> list = new ArrayList<MaterialBean>();
		while (resultSet.next()) {
			material = new MaterialBean();
			material.setId(resultSet.getInt("id"));
			material.setName(resultSet.getString("name"));
			material.setOrigin(resultSet.getString("origin"));
			material.setMApparentState(resultSet.getString("mApparentState"));
			material.setSid(resultSet.getInt("supplierId"));
			material.setSupplier(resultSet.getString("companyName"));
			material.setPrice(resultSet.getBigDecimal("price"));
			material.setUnit(resultSet.getString("unit"));
			material.setCode(resultSet.getString("code"));
			material.setInci_cn(resultSet.getString("inci_cn"));
			material.setInci_en(resultSet.getString("inci_en"));
			material.setApplication(resultSet.getString("application"));
			material.setPackingWay(resultSet.getString("packingWay"));
			material.setChineseName(resultSet.getString("chineseName"));
			list.add(material);
		}
		page.setList(list);
		
		// 查询材料总数
		cstmt = conn.prepareCall("{call materialTotalCount()}");
		resultSet = cstmt.executeQuery();
		if(resultSet.next()) {
			page.setTotalCount(resultSet.getInt(1));
		}
		
		page.setTotalPage((int) Math.ceil(1.0*page.getTotalCount()/page.getCurrentCount()));
		C3P0Utils.close(cstmt, resultSet);
		return page;
	}

	@Override
	public List<SupplierBean> findSupplierName(String keyword)
			throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call pro_Supplier(?)}");
		cstmt.setObject(1, keyword==null?"":keyword);
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<SupplierBean> list = new ArrayList<SupplierBean>();
		SupplierBean supplier = null;
		while (resultSet.next()) {
			supplier = new SupplierBean();
			supplier.setId(resultSet.getInt(1));
			supplier.setCompanyName(resultSet.getString(2));
			list.add(supplier);
		}
		C3P0Utils.close(cstmt, resultSet);
		return list;
	}

	@Override
	public void addAll(List<MaterialBean> getmList) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call insertOrReplaceMaterial(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		for (MaterialBean material : getmList) {
			DBHelper.prepareCall(cstmt,
					null,
					material.getChineseName(),
					material.getName(),
					material.getOrigin(),
					material.getMApparentState(),
					material.getSid(),
					material.getCode(),
					material.getInci_en(),
					material.getInci_cn(),
					material.getApplication(),
					material.getPackingWay(),
					material.getPrice(),
					material.getUnit());
			cstmt.addBatch();
		}
		//批量处理
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public List<String> findAllMaterialCode() throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		PreparedStatement cstmt = conn.prepareStatement("select code from material");
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<String> materialCodes = new ArrayList<String>();
		while(resultSet.next()){
			materialCodes.add(resultSet.getString("code"));
		}
		C3P0Utils.close(cstmt, resultSet);
		return materialCodes;
	}

}
