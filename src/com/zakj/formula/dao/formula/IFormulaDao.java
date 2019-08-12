package com.zakj.formula.dao.formula;

import java.sql.SQLException;
import java.util.List;

import com.zakj.formula.bean.FMaterial;
import com.zakj.formula.bean.FormulaDesc;
import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.PageBean;

public interface IFormulaDao{
	/**
	 * 通过id查找对应的配方详情
	 * @param formulaDesc
	 * @return
	 * @throws SQLException
	 */
	public FormulaDesc findFormulaDescById(Integer id) throws SQLException;
	
	/**
	 * 条件查找配方列表
	 * @param formulaDesc
	 * @param pageBean 
	 * @return
	 * @throws SQLException
	 */
	public PageBean<FormulaDesc> findFormulaDescList(FormulaDesc formulaDesc, PageBean<FormulaDesc> pageBean) throws SQLException;
	
	public int checkFMaterialList(List<FMaterial> fMaterialList) throws SQLException;
	
	/**
	 * 保存一个配方
	 * @param formulaDesc
	 * @throws SQLException
	 */
	public void addFormulaDesc(FormulaDesc formulaDesc) throws SQLException;
	
	/**
	 * author:cwj
	 * @throws SQLException 
	 */
	public List<String> updateFormulaDesc(FormulaDesc formulaDesc) throws SQLException;

	/**
	 * 删除一个配方
	 * @param id
	 * @throws SQLException
	 */
	public void deleteFormulaDesc(Integer id) throws SQLException;

	/**
	 * 找出所有的原料的原料名
	 * @param material
	 * @return
	 * @throws SQLException
	 */
	public List<MaterialBean> findMaterialNameList(MaterialBean material) throws SQLException;
	
	/**
	 * 找出所有的原料
	 * @return
	 * @throws SQLException
	 */
	public List<MaterialBean> findAllMaterialList() throws SQLException;

}
