package com.zakj.formula.test;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.dao.supplier.impl.SupplierDaoImpl;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.ISupplierService;
import com.zakj.formula.service.impl.SupplierServiceImpl;

public class SupplierStoredProdureTest {

	@Test
	public void queryTest() throws SQLException{
		SupplierDaoImpl supplierDao = new SupplierDaoImpl();
		System.out.println(new Gson().toJson( supplierDao.findAll()));
		
		SupplierBean bean = supplierDao.find(2);
		System.out.println(new Gson().toJson(bean));
	}
	
	@Test
	public void deleteTest() throws SQLException{
		SupplierDaoImpl supplierDao = new SupplierDaoImpl();
		supplierDao.delete(17);
	}
	
	@Test
	public void insetTest() throws SQLException{
		SupplierDaoImpl supplierDao = new SupplierDaoImpl();
		SupplierBean supplier = new SupplierBean();
		for(int i = 4; i < 10; i++){
			supplier.setAddress("杭州" + i);
			supplier.setTelephone(1657854+i+"");
			supplier.setContact("heht"+i);
			supplier.setCompanyName("阿里"+i);
			supplierDao.add(supplier);
		}
	}
	
	@Test
	public void updateTest() throws SQLException{
		SupplierDaoImpl supplierDao = new SupplierDaoImpl();
		SupplierBean supplier = new SupplierBean();
		supplier.setId(3);
		supplier.setAddress("吴川");
		supplier.setTelephone(16578423+"");
		supplier.setContact("帅哥");
		supplier.setCompanyName("月饼fef");
		supplierDao.update(supplier);
	}
	
	@Test
	public void likeListTest(){
		ISupplierService service = new SupplierServiceImpl();
		SupplierBean bean = new SupplierBean();
		bean.setTelephone("2");
		PageBean<SupplierBean> pageBean;
		try {
			pageBean = service.findSupplierLikeList(2, 4, bean);
			System.out.println(pageBean);
			System.out.println(pageBean.getList());
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
