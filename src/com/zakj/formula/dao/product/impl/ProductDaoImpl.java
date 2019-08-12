package com.zakj.formula.dao.product.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.sun.xml.internal.ws.org.objectweb.asm.Type;
import com.zakj.formula.bean.FormulaBean;
import com.zakj.formula.bean.PCatogery;
import com.zakj.formula.bean.PSeries;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.dao.product.IProductDao;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.DBHelper;

public class ProductDaoImpl implements IProductDao {

	@Override
	public void add(ProductBean product) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call insertOrReplaceProduct(?,?,?,?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt, 
				null,
				product.getName(),
				product.getNumber(),
				product.getFid(),
				product.getFormula_desc(),
				product.getMajor_composition(),
				product.getSample_quantity(),
				product.getPrice());
		cstmt.registerOutParameter(1, Type.INT);
		cstmt.executeUpdate();
		int pid = cstmt.getInt(1);
		cstmt = conn.prepareCall("{call insert_PSC(?,?,?)}");
		List<PSeries> p_series = product.getP_series();
		if (p_series != null && p_series.size() > 0) {
			for (PSeries pSeries : p_series) {
				cstmt.setObject(1, pid);
				cstmt.setObject(2, pSeries.getId());
				List<PCatogery> p_catogeris = pSeries.getP_catogeris();
				if (p_catogeris != null && p_catogeris.size() > 0) {
					for (PCatogery pCatogery : p_catogeris) {
						cstmt.setObject(3, pCatogery.getId());
						cstmt.addBatch();
					}
				} else {
					cstmt.setObject(3, null);
					cstmt.addBatch();
				}
			}
		}
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public void update(ProductBean product) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call insertOrReplaceProduct(?,?,?,?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt, 
				product.getId(),
				product.getName(),
				product.getNumber(),
				product.getFid(),
				product.getFormula_desc(),
				product.getMajor_composition(),
				product.getSample_quantity(),
				product.getPrice());
		cstmt.registerOutParameter(1, Type.INT);
		cstmt.executeUpdate();
		int pid = cstmt.getInt(1);
		cstmt = conn.prepareCall("{call delete_PSC(?)}");
		cstmt.setObject(1, pid);
		cstmt.executeUpdate();

		cstmt = conn.prepareCall("{call insert_PSC(?,?,?)}");
		List<PSeries> p_series = product.getP_series();
		List<PCatogery> p_catogeris = null;
		if (p_series != null && p_series.size() > 0) {
			for (PSeries pSeries : p_series) {
				cstmt.setObject(1, product.getId());
				cstmt.setObject(2, pSeries.getId());
				p_catogeris = pSeries.getP_catogeris();
				if (p_catogeris != null && p_catogeris.size() > 0) {
					for (PCatogery pCatogery : p_catogeris) {
						cstmt.setObject(3, pCatogery.getId());
						cstmt.addBatch();
					}
				} else {
					cstmt.setObject(3, null);
					cstmt.addBatch();
				}
				cstmt.executeBatch();
			}
		}
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public ProductBean find(Integer id) throws SQLException {
		return null;
	}

	@Override
	public void delete(Integer id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call deleteProduct(?)}");
		cstmt.setObject(1, id);
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public PageBean<ProductBean> findProductList(ProductBean product,
			PageBean<ProductBean> pageBean) throws SQLException {
		if(product.getSid() == null) {
			product.setSid(0);
		}
		if(product.getCid() == null) {
			product.setCid(0);
		}
		
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call pro_ProductPagination(?,?,?,?,?,?)}");
		DBHelper.prepareCall(cstmt, 
				pageBean.getLimit(), 
				pageBean.getCurrentCount(),
				product.getSid()==0?null:product.getSid(),
				product.getCid()==0?null:product.getCid(),
				product.getName()==null?"":product.getName(),
				product.getNumber()==null || product.getNumber() == ""?null:product.getNumber());
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<ProductBean> products = new ArrayList<ProductBean>();
		ProductBean bean;
		while (resultSet.next()) {
			bean = new ProductBean();
			bean.setId(resultSet.getInt("id"));
			bean.setName(resultSet.getString("name"));
			bean.setNumber(resultSet.getString("number"));
			bean.setFid(resultSet.getInt("formula"));
			bean.setFormula_desc(resultSet.getString("formula_desc"));
			bean.setMajor_composition(resultSet.getString("major_composition"));
			bean.setSample_quantity(resultSet.getBigDecimal("sample_quantity"));
			bean.setPrice(resultSet.getBigDecimal("price"));
			products.add(bean);
		}
		
		//查询产品总数
		cstmt = conn.prepareCall("{call productTotalCount()}");
		resultSet = cstmt.executeQuery();
		if(resultSet.next()) {
			pageBean.setTotalCount(resultSet.getInt(1));
		}

		// 给指定的产品查找其所属的系列
		ArrayList<PSeries> p_series = null;
		PSeries pSeries = null;
		for (ProductBean productBean : products) {
			cstmt = conn.prepareCall("{call selectByPid_PS(?)}");
			cstmt.setObject(1, productBean.getId());
			resultSet = cstmt.executeQuery();
			p_series = new ArrayList<PSeries>();
			while (resultSet.next()) {
				pSeries = new PSeries(resultSet.getInt(1),
						resultSet.getString(2), null);
				p_series.add(pSeries);
			}
			productBean.setP_series(p_series);
			
			// 给指定的产品指定的系列查找其所属的类别
			ArrayList<PCatogery> p_catogeris = null;
			PCatogery pCatogery = null;
			for (PSeries pSeries2 : p_series) {
				cstmt = conn.prepareCall("{call select_P_Presentation(?,?)}");
				cstmt.setObject(1, productBean.getId());
				cstmt.setObject(2, pSeries2.getId());
				resultSet = cstmt.executeQuery();
				p_catogeris = new ArrayList<PCatogery>();
				while (resultSet.next()) {
					pCatogery = new PCatogery(resultSet.getInt(1),
							resultSet.getString(2));
					p_catogeris.add(pCatogery);
				}
				pSeries2.setP_catogeris(p_catogeris);
			}
		}
		pageBean.setList(products);
		pageBean.setTotalPage((int) Math.ceil(1.0 * pageBean.getTotalCount()
				/ pageBean.getCurrentCount()));
		C3P0Utils.close(cstmt, resultSet);
		return pageBean;
	}

	@Override
	public List<FormulaBean> findFormulaNameList(String keyword)
			throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call pro_Formula(?)}");
		cstmt.setObject(1, keyword);
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<FormulaBean> list = new ArrayList<FormulaBean>();
		FormulaBean formula = null;
		while (resultSet.next()) {
			formula = new FormulaBean();
			formula.setId(resultSet.getInt(1));
			formula.setName(resultSet.getString(2));
			list.add(formula);
		}
		C3P0Utils.close(cstmt, resultSet);
		return list;
	}

	@Override
	public PageBean<PSeries> findPSAndPCList(PSeries pSeries,
			PageBean<PSeries> pageBean) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();

		// 查找所有系列
		CallableStatement cstmt = conn
				.prepareCall("{call pro_SeriesPagination(?,?)}");
		DBHelper.prepareCall(cstmt, pageBean.getLimit(), pageBean.getCurrentCount());
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<PSeries> p_series = new ArrayList<PSeries>();
		while (resultSet.next()) {
			pSeries = new PSeries(resultSet.getInt(1), resultSet.getString(2), null);
			p_series.add(pSeries);
		}
		
		// 查找所有系列的总数
		cstmt = conn.prepareCall("{call seriesTotalCount()}");
		resultSet = cstmt.executeQuery();
		if (resultSet.next()) {
			pageBean.setTotalCount(resultSet.getInt(1));
		}

		// 查找指定系列的所有类别
		cstmt = conn.prepareCall("{call selectByPSid_PC(?)}");
		ArrayList<PCatogery> p_catogeris = null;
		PCatogery pCatogery = null;
		for (PSeries pSeries1 : p_series) {
			p_catogeris = new ArrayList<PCatogery>();
			cstmt.setObject(1, pSeries1.getId());
			resultSet = cstmt.executeQuery();
			while (resultSet.next()) {
				pCatogery = new PCatogery(resultSet.getInt(1),
						resultSet.getString(2));
				p_catogeris.add(pCatogery);
			}
			pSeries1.setP_catogeris(p_catogeris);
		}
		pageBean.setList(p_series);
		pageBean.setTotalPage((int) Math.ceil(1.0 * pageBean.getTotalCount()
				/ pageBean.getCurrentCount()));
		C3P0Utils.close(cstmt, resultSet);
		return pageBean;
	}

	@Override
	public int addSeries(PSeries pSeries) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call insertP_Series(?,?)}");
		DBHelper.prepareCall(cstmt, null,pSeries.getS_name());
		cstmt.registerOutParameter(1, Type.INT);
		cstmt.executeUpdate();
		int id = cstmt.getInt(1);
		C3P0Utils.close(cstmt, null);
		return id;
	}

	@Override
	public int addCatogery(PCatogery pCatogery, Integer sid)
			throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call insertP_Category(?,?,?)}");
		DBHelper.prepareCall(cstmt, null,pCatogery.getName(),sid);
		cstmt.registerOutParameter(1, Type.INT);
		cstmt.executeUpdate();
		int id = cstmt.getInt(1);
		C3P0Utils.close(cstmt, null);
		return id;
	}

	@Override
	public List<PCatogery> findAllCatogery() throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call selectAllP_Category()}");
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<PCatogery> list = new ArrayList<PCatogery>();
		PCatogery pCatogery = null;
		while (resultSet.next()) {
			pCatogery = new PCatogery(resultSet.getInt(1),
					resultSet.getString(2));
			list.add(pCatogery);
		}
		C3P0Utils.close(cstmt, resultSet);
		return list;
	}

	@Override
	public void updatePS(PSeries pSeries) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		// 修改系列
		CallableStatement cstmt = conn
				.prepareCall("{call updateP_Series(?,?)}");
		DBHelper.prepareCall(cstmt, pSeries.getId(), pSeries.getS_name());
		cstmt.executeUpdate();

		// 删除指定系列的所有类别
		cstmt = conn.prepareCall("{call deletePCBySid(?)}");
		DBHelper.prepareCall(cstmt, pSeries.getId());
		cstmt.executeUpdate();

		// 给指定系列添加类别
		List<PCatogery> catogeris = pSeries.getP_catogeris();
		cstmt = conn.prepareCall("{call insertPC(?,?)}");
		for (PCatogery pCatogery : catogeris) {
			DBHelper.prepareCall(cstmt, pSeries.getId(), pCatogery.getName());
			cstmt.addBatch();
		}
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public PSeries findSeries(Integer id) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		PSeries pSeries = null;
		CallableStatement cstmt = conn.prepareCall("{call selectP_Series(?)}");
		DBHelper.prepareCall(cstmt, id);
		ResultSet resultSet = cstmt.executeQuery();
		while (resultSet.next()) {
			pSeries = new PSeries(resultSet.getInt(1), resultSet.getString(2),
					null);
		}

		cstmt = conn.prepareCall("{call selectByPSid_PC(?)}");
		DBHelper.prepareCall(cstmt, id);
		resultSet = cstmt.executeQuery();
		ArrayList<PCatogery> list = new ArrayList<PCatogery>();
		PCatogery catogery = null;
		while (resultSet.next()) {
			catogery = new PCatogery(resultSet.getInt("id"),
					resultSet.getString("name"));
			list.add(catogery);
		}
		pSeries.setP_catogeris(list);
		C3P0Utils.close(cstmt, null);
		return pSeries;
	}

	@Override
	public void deleteSC(int sid) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call deleteSC(?)}");
		DBHelper.prepareCall(cstmt, sid);
		cstmt.executeUpdate();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public List<PSeries> findAllPSAndPCList() throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call selectAllP_Series()}");
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<PSeries> p_series = new ArrayList<PSeries>();
		PSeries pSeries = null;
		while (resultSet.next()) {
			pSeries = new PSeries(resultSet.getInt(1), resultSet.getString(2),
					null);
			p_series.add(pSeries);
		}

		// 查找指定系列的所有类别
		cstmt = conn.prepareCall("{call selectByPSid_PC(?)}");
		ArrayList<PCatogery> p_catogeris = null;
		PCatogery pCatogery = null;
		for (PSeries pSeries1 : p_series) {
			p_catogeris = new ArrayList<PCatogery>();
			cstmt.setObject(1, pSeries1.getId());
			resultSet = cstmt.executeQuery();
			while (resultSet.next()) {
				pCatogery = new PCatogery(resultSet.getInt(1),
						resultSet.getString(2));
				p_catogeris.add(pCatogery);
			}
			pSeries1.setP_catogeris(p_catogeris);
		}
		C3P0Utils.close(cstmt, null);
		return p_series;
	}

	@Override
	public List<String> findFormulaNumberList(String formula_number) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn.prepareCall("{call pro_FormulaNumber(?)}");
		DBHelper.prepareCall(cstmt, formula_number);
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<String> list = new ArrayList<String>();
		while(resultSet.next()){
			list.add(resultSet.getString("formula_number"));
		}
		return list;
	}

	@Override
	public void addAll(List<ProductBean> getpList) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		PreparedStatement cstmt = conn.prepareStatement(
				"insert into Product(Name,number,formula_desc,major_composition,sample_quantity,price) "
				+ "values(?,?,?,?,?,?)");
		for (ProductBean product : getpList) {
			DBHelper.prepareCall(cstmt, 
					product.getName(),
					product.getNumber(),
					product.getFormula_desc(),
					product.getMajor_composition(),
					product.getSample_quantity(),
					product.getPrice());
			cstmt.addBatch();
		}
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, null);
	}

	@Override
	public List<String> findAllProductName() throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		PreparedStatement cstmt = conn.prepareStatement("select Name from Product");
		ResultSet resultSet = cstmt.executeQuery();
		ArrayList<String> productNames = new ArrayList<String>();
		while (resultSet.next()){
			productNames.add(resultSet.getString("Name"));
		}
		C3P0Utils.close(cstmt, resultSet);
		return productNames;
	}

	@Override
	public void addSeriesCatogery(PSeries pSeries) throws SQLException {
		Connection conn = C3P0Utils.getCurrentConnection();
		CallableStatement cstmt = conn
				.prepareCall("{call insertCategoryBySeriesID(?,?)}");
		
		for(PCatogery category:pSeries.getP_catogeris()) {
			cstmt.setObject(1, pSeries.getId());
			cstmt.setObject(2, category.getName());
			cstmt.addBatch();
		}
		cstmt.executeBatch();
		C3P0Utils.close(cstmt, null);
		
	}
}
