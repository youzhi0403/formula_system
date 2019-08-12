package com.zakj.formula.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.exception.CustomException;

public interface ISupplierService {
	/**
	 * 添加供应商
	 * 
	 * @param supplier
	 * @throws CustomException 
	 */
	public void addSupplier(SupplierBean supplier) throws CustomException;
	
	/**
	 * 更新供应商
	 * 
	 * @param supplier
	 * @return void
	 * @throws CustomException 
	 */
	public void updateSupplier(SupplierBean supplier) throws CustomException;
	
	/**
	 * 删除供应商
	 * 
	 * @param id
	 * @throws CustomException 
	 */
	public void deleteSupplier(Integer id) throws CustomException;
	
	/**
	 * 查询一条数据
	 * 
	 * @param id
	 * @return
	 * @throws CustomException 
	 */
	public SupplierBean find(Integer id) throws CustomException;
	
	/**
	 * 查询全部
	 * 
	 * @return
	 * @throws CustomException 
	 */
	public List<SupplierBean> findAllSupplier() throws CustomException;

	/**
	 * 模糊分页查询
	 * 
	 * @param currentPage 当前页
	 * @param currentCount 当前显示记录数
	 * @param supplier 供应商对象
	 * @return PageBean<SupplierBean> supplier供应商的列表页面对象
	 * @throws CustomException 
	 */
	public PageBean<SupplierBean> findSupplierLikeList(int currentPage,
			int currentCount, SupplierBean supplier) throws CustomException;

	public void parseExcel(InputStream is) throws CustomException, IOException;
}
