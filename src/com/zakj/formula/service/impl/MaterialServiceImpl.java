package com.zakj.formula.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.dao.material.IMaterialDao;
import com.zakj.formula.dao.material.impl.MaterialDaoImpl;
import com.zakj.formula.dao.supplier.impl.SupplierDaoImpl;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IMaterialService;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.MaterialExcelParseHelper;
import com.zakj.formula.utils.POIUtils;

public class MaterialServiceImpl implements IMaterialService {

	private IMaterialDao dao = null;

	public MaterialServiceImpl() {
		dao = new MaterialDaoImpl();
	}

	@Override
	public void addMaterial(MaterialBean material) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.add(material);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_ADD);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void updateMaterial(MaterialBean material) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.update(material);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_UPDATE);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void deleteMaterial(Integer id) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.delete(id);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("该原料被其他配方占用，无法删除！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public PageBean<MaterialBean> findMaterialList(int currentPage,
			int currentCount, MaterialBean material) throws CustomException {
		PageBean<MaterialBean> pageBean = new PageBean<MaterialBean>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setCurrentCount(currentCount);
		try {
			C3P0Utils.beginTransation();
			pageBean = dao.findMaterialList(material, pageBean);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
		return pageBean;
	}

	@Override
	public List<SupplierBean> getSupplierNameList(String keywrod)
			throws CustomException {
		try {
			C3P0Utils.beginTransation();
			List<SupplierBean> list = dao.findSupplierName(keywrod);
			return list;
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void parseExcel(InputStream is) throws IOException, CustomException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
		try {
			// 判断供应商是否存在
			List<String> materialCodes = dao.findAllMaterialCode();

			MaterialExcelParseHelper helper = new MaterialExcelParseHelper();
			for (Row row : hssfSheet) {
				System.out.println();
				for (Cell cell : row) {
					System.out.println(POIUtils
							.getHSSFCellStringValue((HSSFCell) cell));
					helper.parseXls(cell.getRowIndex(), cell.getColumnIndex(),
							POIUtils.getHSSFCellStringValue((HSSFCell) cell), materialCodes);
				}
			}

			if (helper.getmList().size() == 0) {
				throw new CustomException("该表为空表！");
			}

			C3P0Utils.beginTransation();
			ArrayList<SupplierBean> supplierList = new SupplierDaoImpl()
					.findAll();
			List<String> supplierNames = new ArrayList<String>();
			for (SupplierBean supplierBean : supplierList) {
				supplierNames.add(supplierBean.getCompanyName());
			}
			for (MaterialBean material : helper.getmList()) {
				int index = supplierNames.indexOf(material.getSupplier());
				if (index == -1) {
					throw new CustomException("第"
							+ (helper.getmList().indexOf(material) + 2)
							+ "行，供应商：" + material.getSupplier() + ",不存在数据库中！");
				}
				material.setSid(supplierList.get(
						supplierNames.indexOf(material.getSupplier())).getId());
			}
			dao.addAll(helper.getmList());
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("原因不详！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}
}
