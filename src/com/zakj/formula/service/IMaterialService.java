package com.zakj.formula.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.exception.CustomException;

public interface IMaterialService {

	public void addMaterial(MaterialBean material) throws CustomException;
	
	public void updateMaterial(MaterialBean material) throws CustomException;
	
//	public PageBean<MaterialBean> findMaterialList(Integer currentPage, Integer currentCount) throws CustomException;
	
	public PageBean<MaterialBean> findMaterialList(int currentPage, int currentCount, MaterialBean material) throws CustomException;
	
	public void deleteMaterial(Integer id) throws CustomException;

	public List<SupplierBean> getSupplierNameList(String keywrod) throws CustomException;

	public void parseExcel(InputStream is) throws IOException, CustomException ;
}
