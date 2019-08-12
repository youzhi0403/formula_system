package com.zakj.formula.test;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.Test;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.dao.material.impl.MaterialDaoImpl;

public class MaterialStoredProdureTest {
	
	@Test
	public void findAll(){
		
	}
	
	@Test
	public void insertTest() throws SQLException{
		MaterialDaoImpl service = new MaterialDaoImpl();
		MaterialBean material = new MaterialBean();
		material.setName("人參");
		material.setOrigin("高原雪山");
		material.setPrice(new BigDecimal("3.5"));
		material.setSid(40);
		material.setMApparentState("苦涩");
		material.setUnit("克");
		service.add(material);
	}
	
	@Test
	public void updateTest() throws SQLException{
		MaterialDaoImpl service = new MaterialDaoImpl();
		MaterialBean material = new MaterialBean();
		material.setId(11);
		material.setName("人參");
		material.setOrigin("高原雪山");
		material.setPrice(new BigDecimal("5.5"));
		material.setSid(41);
		material.setMApparentState("苦涩");
		material.setUnit("克");
		service.update(material);
	}
}
