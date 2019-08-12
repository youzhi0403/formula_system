package com.zakj.formula.test;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import com.zakj.formula.bean.PSeries;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.dao.product.IProductDao;
import com.zakj.formula.dao.product.impl.ProductDaoImpl;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IProductService;
import com.zakj.formula.service.impl.ProductServiceImpl;
import com.zakj.formula.utils.C3P0Utils;

public class ProductTest {

	@Test
	public void testUpdate() throws SQLException{
		IProductDao productDao = new ProductDaoImpl();
		
		ProductBean bean = new ProductBean();
		bean.setId(1);
		PSeries s = null;
		ArrayList<PSeries> list = new ArrayList<PSeries>();
		for (int i = 1; i < 4; i++) {
			s = new PSeries();
			s.setId(i);
			s.setS_name("系列"+i);
			list.add(s);
		}
		bean.setP_series(list);
		C3P0Utils.beginTransation();
		productDao.update(bean);
		C3P0Utils.commitAndRelease();
	}
	
	@Test
	public void testFind() throws SQLException, CustomException{
		ProductBean bean = new ProductBean();
		bean.setId(1);
		IProductService service = new ProductServiceImpl();
		PageBean<ProductBean> pageBean = service.showProductList(1, 20, bean);
		System.out.println(pageBean);
	}
	
}
