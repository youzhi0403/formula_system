package com.zakj.formula.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.dao.supplier.ISupplierDao;
import com.zakj.formula.dao.supplier.impl.SupplierDaoImpl;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.ISupplierService;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.POIUtils;
import com.zakj.formula.utils.ProductExcelParseHelper;
import com.zakj.formula.utils.SupplierExcelParseHelper;

public class SupplierServiceImpl implements ISupplierService {

	private ISupplierDao dao;

	public SupplierServiceImpl() {
		dao = new SupplierDaoImpl();
	}

	@Override
	public void addSupplier(SupplierBean supplier) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.add(supplier);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_ADD);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void updateSupplier(SupplierBean supplier) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.update(supplier);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_UPDATE);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void deleteSupplier(Integer id) throws CustomException {

		try {
			C3P0Utils.beginTransation();
			dao.delete(id);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("该供应商被其他原料占用，无法删除！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public PageBean<SupplierBean> findSupplierLikeList(int currentPage,
			int currentCount, SupplierBean supplier) throws CustomException {
		PageBean<SupplierBean> pageBean = new PageBean<SupplierBean>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setCurrentCount(currentCount);
		try {
			C3P0Utils.beginTransation();
			pageBean = dao.likeName(supplier, pageBean);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
		return pageBean;
	}

	// @Override
	// public PageBean<SupplierBean> findSupplierList(int currentPage, int
	// currentCount) {
	// PageBean<SupplierBean> pageBean = new PageBean<SupplierBean>();
	// pageBean.setCurrentPage(currentPage);
	// pageBean.setCurrentCount(currentCount);
	//
	// try {
	// int totalCount = dao.getCount();
	// int totalPage = (2*totalCount-1)/currentCount;
	// pageBean.setTotalCount(totalCount);
	// pageBean.setTotalPage(totalPage);
	//
	// int start = (currentPage-1)*currentCount;
	// List<SupplierBean> list = dao.findList(start, start+currentCount);
	// pageBean.setList(list);
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return pageBean;
	// }

	@Override
	public List<SupplierBean> findAllSupplier() throws CustomException {
		ISupplierDao dao = new SupplierDaoImpl();
		List<SupplierBean> list = null;
		try {
			C3P0Utils.beginTransation();
			list = dao.findAll();
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
		return list;
	}

	@Override
	public SupplierBean find(Integer id) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			return dao.find(id);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void parseExcel(InputStream is) throws CustomException, IOException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
		try {
			// 用于判断是否重复导入相同的供应商
			List<String> supplierNames = findAllSupplierName();

			SupplierExcelParseHelper helper = new SupplierExcelParseHelper();
			for (Row row : hssfSheet) {
				System.out.println();
				for (Cell cell : row) {
					helper.parseXls(cell.getRowIndex(), cell.getColumnIndex(),
							POIUtils.getHSSFCellStringValue((HSSFCell) cell),supplierNames);
				}
			}

			if (helper.getsList().size() == 0) {
				throw new CustomException("该表为空表！");
			}

			C3P0Utils.beginTransation();
			dao.addAll(helper.getsList());
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("原因不详！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	private List<String> findAllSupplierName() throws SQLException {
		return dao.findAllSupplierName();
	}

}
