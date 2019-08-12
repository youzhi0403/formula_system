package com.zakj.formula.dao.product;

import java.sql.SQLException;
import java.util.List;

import com.zakj.formula.bean.FormulaBean;
import com.zakj.formula.bean.PCatogery;
import com.zakj.formula.bean.PSeries;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.dao.IBaseDao;

public interface IProductDao extends IBaseDao<ProductBean>{
	public PageBean<ProductBean> findProductList(ProductBean product, PageBean<ProductBean> pageBean) throws SQLException;
	
	/**
	 * 查询所有的配方名
	 * @param keyword
	 * @return
	 * @throws SQLException
	 */
	public List<FormulaBean> findFormulaNameList(String keyword) throws SQLException;
	
	/**
	 * 查找所有的系列和所有对应的类别
	 * @param pageBean 
	 * @param pSeries 
	 * @return
	 * @throws SQLException
	 */
	public PageBean<PSeries> findPSAndPCList(PSeries pSeries, PageBean<PSeries> pageBean) throws SQLException;

	/**
	 * 添加类别
	 * @param pCatogery
	 * @throws SQLException
	 */
	public int addCatogery(PCatogery pCatogery, Integer sid) throws SQLException;
	
	/**
	 * 添加系列
	 * @param pSeries
	 * @throws SQLException
	 */
	public int addSeries(PSeries pSeries) throws SQLException;
	
	/**
	 * 查找数据库中的所有的类别
	 * @return 
	 * @throws SQLException 
	 */
	public List<PCatogery> findAllCatogery() throws SQLException;
	
	/**
	 * 更新系列
	 * @param pSeries
	 * @throws SQLException 
	 */
	public void updatePS(PSeries pSeries) throws SQLException;

	public PSeries findSeries(Integer id) throws SQLException;

	public void deleteSC(int sid) throws SQLException;

	public List<PSeries> findAllPSAndPCList() throws SQLException;

	public List<String> findFormulaNumberList(String formula_number) throws SQLException;

	public void addAll(List<ProductBean> getpList) throws SQLException;

	public List<String> findAllProductName() throws SQLException;

	public void addSeriesCatogery(PSeries pSeries) throws SQLException;
	
}
