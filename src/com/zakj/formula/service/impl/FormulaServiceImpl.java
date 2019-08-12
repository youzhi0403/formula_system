package com.zakj.formula.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zakj.formula.bean.FMaterial;
import com.zakj.formula.bean.FormulaDesc;
import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.dao.formula.IFormulaDao;
import com.zakj.formula.dao.formula.impl.FormulaDaoImpl;
import com.zakj.formula.dao.material.impl.MaterialDaoImpl;
import com.zakj.formula.dao.product.IProductDao;
import com.zakj.formula.dao.product.impl.ProductDaoImpl;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IFormulaService;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.ExcelExportHelper;
import com.zakj.formula.utils.FormulaExcelParseHelper;
import com.zakj.formula.utils.POIUtils;
import com.zakj.formula.utils.UnitHandler;

public class FormulaServiceImpl implements IFormulaService {

	private IFormulaDao dao;

	public FormulaServiceImpl() {
		dao = new FormulaDaoImpl();
	}

	@Override
	public PageBean<FormulaDesc> getFormulaDescList(int currentPage,
			int currentCount, FormulaDesc formulaDesc) throws CustomException {
		PageBean<FormulaDesc> pageBean = new PageBean<FormulaDesc>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setCurrentCount(currentCount);
		pageBean = calculatePrice(formulaDesc, pageBean);
		return pageBean;
	}

	/**
	 * 计算每个配方的单价价格，并将单价封装到配方实体中
	 * 
	 * @param formulaDesc
	 *            封装了查询formula的条件的formula对象
	 * @param pageBean
	 * @return 每条配方都包含原料列表信息的配方列表
	 * @throws CustomException
	 * @throws SQLException
	 */
	private PageBean<FormulaDesc> calculatePrice(FormulaDesc formulaDesc,
			PageBean<FormulaDesc> pageBean) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			pageBean = dao.findFormulaDescList(formulaDesc, pageBean);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
		BigDecimal price = BigDecimal.ZERO;
		BigDecimal finalUnitPrice = BigDecimal.ZERO;
		BigDecimal planAmount = BigDecimal.ZERO;
		// 遍历所有配方，并计算每个配方的配方成本
		for (FormulaDesc formulaDesc1 : pageBean.getList()) {
			// 计算每一个原料用量 所需要的价格
			for (FMaterial fMaterial : formulaDesc1.getFmlist()) {
				//转换单位，转换为：千克/元
				finalUnitPrice = UnitHandler.translate(fMaterial.getUnit(),
						UnitHandler.KILOGRAM, fMaterial.getPrice());
				// 计算原料成本：单价（元/千克）*实称量（千克）
				price = (finalUnitPrice.multiply(fMaterial.getPlan_amount()))
						.add(price);
				//累计原料的计划量
				planAmount = planAmount.add(fMaterial.getPlan_amount());
			}
			// 配方成本统一使用（元/千克）单位，所以price需要除以计划量
			formulaDesc1.setPrice(price);
			price = BigDecimal.ZERO;
			finalUnitPrice = BigDecimal.ZERO;
			planAmount = BigDecimal.ZERO;
			formulaDesc1.setFmlist(null);
		}
		return pageBean;
	}

	@Override
	public List<MaterialBean> getMaterialNameList(MaterialBean material)
			throws CustomException {
		try {
			C3P0Utils.beginTransation();
			return dao.findMaterialNameList(material);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public FormulaDesc getFormulaDescById(Integer id) throws CustomException {
		FormulaDesc formulaDesc = null;
		try {
			C3P0Utils.beginTransation();
			formulaDesc = dao.findFormulaDescById(id);
			if (formulaDesc == null) {
				throw new CustomException("没有该条配方详情信息！");
			}
			// 查找配方中的原料的INCI，并放入配方中的原料里
			List<FMaterial> fmlist = formulaDesc.getFmlist();
			BigDecimal finalUnitPrice;
			for (FMaterial fMaterial : fmlist) {
				// 统一将单位更改为:元/千克
				finalUnitPrice = UnitHandler.translate(fMaterial.getUnit(),
						UnitHandler.KILOGRAM, fMaterial.getPrice());
				fMaterial.setPrice(finalUnitPrice);
				fMaterial.setUnit(UnitHandler.KILOGRAM);
			}
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
		// 将工艺记录切割成列表
		String attention_item = formulaDesc.getAttention_item();
		if (attention_item != null) {
			String[] items = attention_item.split("<>");
			formulaDesc.setAttentionList(Arrays.asList(items));
		}

		// 将注意事项切割成列表
		String technology_proc = formulaDesc.getTechnology_proc();
		if (technology_proc != null) {
			String[] procs = technology_proc.split("<>");
			formulaDesc.setProcList(Arrays.asList(procs));
		}

		formulaDesc.setAttention_item(null);
		formulaDesc.setTechnology_proc(null);
		return formulaDesc;
	}

	@Override
	public void parseXlsx(InputStream is) throws CustomException {
		XSSFWorkbook workbook;
		FormulaDesc formulaDesc = null;
		try {
			workbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CustomException("excel文件无法解析，可能结构已被破坏！");
		}

		// 循环解析每一个sheet
		FormulaExcelParseHelper helper = null;
		ArrayList<FormulaDesc> formulaDescList = new ArrayList<>();
		int sheets = workbook.getNumberOfSheets();
		for (int i = 0; i < sheets; i++) {
			helper = new FormulaExcelParseHelper();
			XSSFSheet xssfSheet = workbook.getSheetAt(i);
			for (Row row : xssfSheet) {
				for (Cell cell : row) {
					int rowIndex = cell.getRowIndex();
					int columnIndex = cell.getColumnIndex();
					try {
						helper.handleCell(rowIndex, columnIndex, POIUtils
								.getXSSFCellStringValue((XSSFCell) cell), cell);
					} catch (CustomException e) {
						throw new CustomException("导入失败，"
								+ xssfSheet.getSheetName() + ","
								+ e.getMessage());
					}
				}
			}
			formulaDesc = helper.getFormulaDesc();
			formulaDesc.setFmlist(helper.getList());
			helper.setMaterialList(true);
			for (Row row : xssfSheet) {
				for (Cell cell : row) {
					int rowIndex = cell.getRowIndex();
					int columnIndex = cell.getColumnIndex();
					helper.handleRightCell(rowIndex, columnIndex,
							POIUtils.getXSSFCellStringValue((XSSFCell) cell));
				}
			}

			IProductDao productDao = new ProductDaoImpl();
			try {
				C3P0Utils.beginTransation();
				// 从数据库中查找该配方的编号
				List<String> numberList = productDao
						.findFormulaNumberList(formulaDesc.getF_number());
				if (numberList.size() > 0) {
					throw new CustomException("配方编号为“"
							+ formulaDesc.getF_number() + "”已存在。");
				}
			} catch (SQLException e) {
				C3P0Utils.rollback();
				e.printStackTrace();
				throw new CustomException("获取原料信息失败！");
			} finally {
				C3P0Utils.commitAndRelease();
			}

			// 判断配方中的原料是否存在数据库中
			List<MaterialBean> materialInfolist = null;
			try {
				C3P0Utils.beginTransation();
				materialInfolist = dao.findAllMaterialList();
			} catch (SQLException e) {
				C3P0Utils.rollback();
				e.printStackTrace();
				throw new CustomException("获取原料信息失败！");
			} finally {
				C3P0Utils.commitAndRelease();
			}
			ArrayList<String> materialCodes = new ArrayList<String>();
			for (MaterialBean materialBean : materialInfolist) {
				materialCodes.add(materialBean.getCode());
			}
			for (FMaterial fMaterial : helper.getFormulaDesc().getFmlist()) {
				if (!materialCodes.contains(fMaterial.getCode())) {
					throw new CustomException(xssfSheet.getSheetName() + "  "
							+ "原料“" + fMaterial.getCode() + "”不在数据库中！");
				} else {
					// //判断原料商品名是否正确
					// if(!fMaterial.getName()
					// .equals(materialInfolist.get(materialCodes.indexOf(fMaterial.getCode())).getName())){
					// throw new
					// CustomException("原料代码为"+fMaterial.getCode()+"的原料名'"+fMaterial.getName()+"'与数据库的数据'"+materialInfolist.get(materialCodes.indexOf(fMaterial.getCode())).getName()+"'不一致。");
					// }
					// if(fMaterial.getCn_name() != null){
					// if(!fMaterial.getInci_cn().equals(
					// materialInfolist.get(materialCodes.indexOf(fMaterial.getCode())).getInci_cn())){
					// throw new
					// CustomException("原料代码为"+fMaterial.getCode()+"的中文INCI名与数据库的数据不一致。");
					// }
					// }
					fMaterial.setMid(materialInfolist.get(materialCodes.indexOf(fMaterial.getCode()))
									.getId());
				}
			}
			formulaDescList.add(helper.getFormulaDesc());
		}
		writeToDB(formulaDescList);
	}

	@Override
	public void parseXls(InputStream is) throws CustomException {
		FormulaDesc formulaDesc = null;
		HSSFWorkbook workbook;
		try {
			workbook = new HSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CustomException("excel文件格式错误，可能结构已被破坏！");
		}

		// 循环解析每一个sheet
		FormulaExcelParseHelper helper = null;
		ArrayList<FormulaDesc> formulaDescList = new ArrayList<>();
		int sheets = workbook.getNumberOfSheets();
		System.out.println(sheets);
		for (int i = 0; i < sheets; i++) {
			helper = new FormulaExcelParseHelper();
			HSSFSheet hssfSheet = workbook.getSheetAt(i);
			if (!hssfSheet.rowIterator().hasNext()) {
				throw new CustomException("该配方表为空表！");
			}
			// 第一遍遍历整张表，解析表头和表尾信息，并解析表左中的信息
			for (Row row : hssfSheet) {
				for (Cell cell : row) {
					int rowIndex = cell.getRowIndex();
					int columnIndex = cell.getColumnIndex();
					try {
						helper.handleCell(rowIndex, columnIndex, POIUtils
								.getHSSFCellStringValue((HSSFCell) cell), cell);
					} catch (CustomException e) {
						// 捕获异常，给异常提示信息添加sheet的信息，再重新抛出异常
						throw new CustomException(hssfSheet.getSheetName()
								+ "  " + e.getMsg());
					}
				}
			}
			formulaDesc = helper.getFormulaDesc();
			formulaDesc.setFmlist(helper.getList());
			helper.setMaterialList(true);
			// 第二遍遍历整张表，解析表右中的信息
			for (Row row : hssfSheet) {
				for (Cell cell : row) {
					int rowIndex = cell.getRowIndex();
					int columnIndex = cell.getColumnIndex();
					helper.handleRightCell(rowIndex, columnIndex,
							POIUtils.getHSSFCellStringValue((HSSFCell) cell));
				}
			}

			IProductDao productDao = new ProductDaoImpl();
			try {
				C3P0Utils.beginTransation();
				// 从数据库中查找该配方的编号
				List<String> numberList = productDao
						.findFormulaNumberList(formulaDesc.getF_number());
				if (numberList.size() > 0) {
					throw new CustomException("配方编号为“"
							+ formulaDesc.getF_number() + "”已存在。");
				}
			} catch (SQLException e) {
				C3P0Utils.rollback();
				e.printStackTrace();
				throw new CustomException("获取原料信息失败！");
			} finally {
				C3P0Utils.commitAndRelease();
			}

			// 判断配方中的原料是否存在数据库中
			List<MaterialBean> materialInfolist = null;
			try {
				C3P0Utils.beginTransation();
				materialInfolist = dao.findAllMaterialList();
			} catch (SQLException e) {
				C3P0Utils.rollback();
				e.printStackTrace();
				throw new CustomException("数据库异常！");
			} finally {
				C3P0Utils.commitAndRelease();
			}
			ArrayList<String> materialCodes = new ArrayList<String>();
			for (MaterialBean materialBean : materialInfolist) {
				materialCodes.add(materialBean.getCode());
			}
			for (FMaterial fMaterial : helper.getFormulaDesc().getFmlist()) {
				if (!materialCodes.contains(fMaterial.getCode())) {
					throw new CustomException(hssfSheet.getSheetName() + "  "
							+ "原料“" + fMaterial.getCode() + "”不在数据库中！");
				} else {
					// //判断原料商品名是否正确
					// if(!fMaterial.getName()
					// .equals(materialInfolist.get(materialCodes.indexOf(fMaterial.getCode())).getName())){
					// throw new
					// CustomException("原料代码为"+fMaterial.getCode()+"的原料名'"+fMaterial.getName()+"'与数据库的数据'"+materialInfolist.get(materialCodes.indexOf(fMaterial.getCode())).getName()+"'不一致。");
					// }
					// if(fMaterial.getCn_name() != null){
					// if(!fMaterial.getInci_cn().equals(
					// materialInfolist.get(materialCodes.indexOf(fMaterial.getCode())).getInci_cn())){
					// throw new
					// CustomException("原料代码为"+fMaterial.getCode()+"的中文INCI名与数据库的数据不一致。");
					// }
					// }
					fMaterial
							.setMid(materialInfolist.get(
									materialCodes.indexOf(fMaterial.getCode()))
									.getId());
				}
			}
			formulaDescList.add(helper.getFormulaDesc());
		}
		writeToDB(formulaDescList);
	}

	private void writeToDB(List<FormulaDesc> formulaDescList)
			throws CustomException {
		try {
			C3P0Utils.beginTransation();
			for (FormulaDesc formulaDesc : formulaDescList) {
				dao.addFormulaDesc(formulaDesc);
			}
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("配方数据写入数据库失败！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public HSSFWorkbook getFormulaDescXlsExcel(int fid, String demoPath)
			throws CustomException {

		ExcelExportHelper exportHelper = new ExcelExportHelper();
		HSSFWorkbook workbook;
		try {
			workbook = exportHelper.createXlsExcel(fid, dao, demoPath);
			return workbook;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new CustomException("导出失败，服务器端缺少模板！");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	/**
	 * author:cwj
	 */
	public List<String> updateFormulaDesc(FormulaDesc formulaDesc)
			throws CustomException {
		List<String> list = new ArrayList<String>();
		try {
			C3P0Utils.beginTransation();
			list = dao.updateFormulaDesc(formulaDesc);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("更新失败！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
		return list;
	}

	@Override
	public void deleteFormulaDesc(Integer id) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.deleteFormulaDesc(id);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("删除失败！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

}
