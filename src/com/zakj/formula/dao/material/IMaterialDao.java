package com.zakj.formula.dao.material;

import java.sql.SQLException;
import java.util.List;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.dao.IBaseDao;

public interface IMaterialDao extends IBaseDao<MaterialBean>{
	public PageBean<MaterialBean> findMaterialList(MaterialBean material, PageBean<MaterialBean> page) throws SQLException;
	public List<SupplierBean> findSupplierName(String keywrod) throws SQLException;
	public void addAll(List<MaterialBean> getmList) throws SQLException;
	public List<String> findAllMaterialCode() throws SQLException;
}
