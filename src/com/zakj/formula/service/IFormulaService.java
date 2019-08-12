package com.zakj.formula.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.zakj.formula.bean.FormulaBean;
import com.zakj.formula.bean.FormulaDesc;
import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.exception.CustomException;

public interface IFormulaService {
	
	/**
	 * 查找所有的配方的基本信息，并封装到分页实体中
	 * @param currentPage
	 * @param currentCount
	 * @param formulaDesc
	 * @return
	 * @throws CustomException
	 */
	public PageBean<FormulaDesc> getFormulaDescList(int currentPage, int currentCount, FormulaDesc formulaDesc) throws CustomException;
	
	/**
	 * 查找所有的原料名称
	 * @param material 封装了原料名称的实体
	 * @return
	 * @throws CustomException
	 */
	public List<MaterialBean> getMaterialNameList(MaterialBean material) throws CustomException;
	
	/**
	 * 获取配方详情列表
	 * @param formulaDesc
	 * @return
	 * @throws CustomException
	 */
	public FormulaDesc getFormulaDescById(Integer id) throws CustomException;
	
	/**
	 * 解析97-2003（.xls）的excel文件
	 * @param is
	 * @throws IOException
	 * @throws CustomException
	 */
	public void parseXls(InputStream is) throws IOException, CustomException;

	/**
	 * 解析2003以后（.xlsx）的excel文件
	 * @param is
	 * @throws IOException
	 * @throws CustomException
	 */
	public void parseXlsx(InputStream is) throws IOException, CustomException;

	/**
	 * 返回需要导出的xls格式的excel文件的输出流
	 * @param fid 
	 * 
	 * @return
	 * @throws CustomException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public HSSFWorkbook getFormulaDescXlsExcel(int fid, String demoPath) throws CustomException, FileNotFoundException, IOException;
	
	/**
	 * author:cwj
	 * @throws CustomException 
	 */
	public List<String> updateFormulaDesc(FormulaDesc formulaDesc) throws CustomException;

	public void deleteFormulaDesc(Integer id) throws CustomException;

}
