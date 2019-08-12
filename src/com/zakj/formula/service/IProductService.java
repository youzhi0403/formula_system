package com.zakj.formula.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.zakj.formula.bean.FormulaBean;
import com.zakj.formula.bean.PCatogery;
import com.zakj.formula.bean.PSeries;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.exception.CustomException;

public interface IProductService {
	public PageBean<ProductBean> showProductList(int currentPage, int currentCount, ProductBean product) throws CustomException;
	
	public void updateProduct(ProductBean product) throws CustomException;
	
	public void addProduct(ProductBean product) throws CustomException;
	
	public List<FormulaBean> getFormulaNameList(String keyword) throws CustomException;
	
	public PageBean<PSeries> showPCPage(int currentPage, int currentCount, PSeries pSeries) throws CustomException;

	public int addOrUpdateSeriesAndCatogery(PSeries pSeries) throws CustomException;

	public PSeries findSC(PSeries pSeries) throws CustomException;

	public List<Map<String, Object>> getAllSCZtree() throws CustomException;

	public void deleteProduct(List<Map<String, Integer>> ids) throws CustomException;

	public void deleteSC(List<Map<String,Integer>> sids) throws CustomException;
	
	public List<PSeries> showSCList() throws CustomException;

	public List<String> showFormulaNumberList(String formula_number) throws CustomException;

	public void parseXls(InputStream is) throws IOException, CustomException;

	public void parseXlsx(InputStream inputStream) throws IOException, CustomException;

}
