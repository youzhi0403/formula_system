package com.zakj.formula.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zakj.formula.bean.FMaterial;
import com.zakj.formula.bean.FormulaBean;
import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.MaterialFormulaBean;
import com.zakj.formula.bean.PCatogery;
import com.zakj.formula.bean.PSeries;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.dao.formula.impl.FormulaDaoImpl;
import com.zakj.formula.dao.product.IProductDao;
import com.zakj.formula.dao.product.impl.ProductDaoImpl;
import com.zakj.formula.dao.supplier.impl.SupplierDaoImpl;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IProductService;
import com.zakj.formula.utils.C3P0Utils;
import com.zakj.formula.utils.MaterialExcelParseHelper;
import com.zakj.formula.utils.POIUtils;
import com.zakj.formula.utils.ProductExcelParseHelper;
import com.zakj.formula.utils.UnitHandler;

public class ProductServiceImpl implements IProductService {

	private IProductDao dao;

	public ProductServiceImpl() {
		dao = new ProductDaoImpl();
	}

	@Override
	public PageBean<ProductBean> showProductList(int currentPage,
			int currentCount, ProductBean product) throws CustomException {
		PageBean<ProductBean> pageBean = new PageBean<ProductBean>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setCurrentCount(currentCount);
		// 给产品计算价格
		try {
			C3P0Utils.beginTransation();
			pageBean = dao.findProductList(product, pageBean); // 查找所有的产品
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_ADD);
		} finally {
			C3P0Utils.commitAndRelease();
		}
		// 给产品列表的系列和类别信息添加pid，封装成ztree格式数据
		seriesToZtree(pageBean.getList());
		return pageBean;
	}

	// 给产品列表的系列和类别信息添加pid，封装成ztree格式数据
	private void seriesToZtree(List<ProductBean> productlist)
			throws CustomException {
		Map<String, Object> map = null;
		List<PSeries> allPsList = null;
		try {
			// load出全部系列和类别
			C3P0Utils.beginTransation();
			allPsList = dao.findAllPSAndPCList();
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("获取产品的系列和类别信息失败！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
		ArrayList<Map<String, Object>> ztreeList = null;
		List<PCatogery> p_catogeris = null;
		List<PSeries> p_series = null;
		int id = 0;// 负责记录ztree的id
		int pid = 0;// 负责记录ztree的父id
		for (ProductBean productBean : productlist) {
			// 封装ztree数据的map列表
			ztreeList = new ArrayList<Map<String, Object>>();
			// load出被选择的系列和类别
			p_series = productBean.getP_series();
			// 循环所有的系列
			for (int i = 0; i < allPsList.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("id", ++id);
				map.put("pId", 0);
				map.put("name", allPsList.get(i).getS_name());
				map.put("sid", allPsList.get(i).getId());
				// 与产品所属系列比较，被选择的系列设置checked属性为true，默认打钩
				for (int j = 0; j < p_series.size(); j++) {
					if (p_series.get(j).getId() == allPsList.get(i).getId()) {
						map.put("checked", true);
					}
				}
				pid = id;
				ztreeList.add(map);// 添加一个系列节点

				p_catogeris = allPsList.get(i).getP_catogeris();
				// 循环所有的类别
				for (int j = 0; j < p_catogeris.size(); j++) {
					map = new HashMap<String, Object>();
					map.put("id", ++id);
					map.put("pId", pid);// 给pid指向系列的id，表示该类别在指定id系列的节点下
					map.put("name", p_catogeris.get(j).getName());
					map.put("cid", p_catogeris.get(j).getId());
					// 与产品所属类别比较，被选择的类别设置checked属性为true，默认打钩
					for (PSeries pSeries : p_series) {
						for (PCatogery pCatogery : pSeries.getP_catogeris()) {
							if (allPsList.get(i).getId() == pSeries.getId()
									&& pCatogery.getId() == p_catogeris.get(j)
											.getId()) {
								map.put("checked", true);
							}
						}
					}
					ztreeList.add(map);// 添加一个类别节点
				}
			}
			// 清空系列和类别的信息
			productBean.setP_series(null);
			// 增加一个系列和类别的ztree格式数据
			productBean.setZtree(ztreeList);
			ztreeList = null;
		}
	}

	@Override
	public void updateProduct(ProductBean product) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			dao.update(product);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_UPDATE);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void addProduct(ProductBean product) throws CustomException {
		try {
			// 开始事务
			C3P0Utils.beginTransation();
			dao.add(product);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_ADD);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	// private PageBean<ProductBean> addPriceToProductList(ProductBean
	// product,PageBean<ProductBean> pageBean)
	// throws CustomException {
	// FormulaDaoImpl formulaDao = new FormulaDaoImpl();
	// List<ProductBean> productList = null;
	// List<FMaterial> allFMList = null;
	// float finalUnitPrice = 0f;
	// float price = 0f;
	// try {
	// C3P0Utils.beginTransation();
	// pageBean = dao.findProductList(product, pageBean); // 查找所有的产品
	// productList = pageBean.getList();
	// allFMList = formulaDao.findAllFMList();
	// } catch (SQLException e) {
	// C3P0Utils.rollback();
	// e.printStackTrace();
	// throw new CustomException(CustomException.SQL_EXCEPTION_ADD);
	// } finally {
	// C3P0Utils.commitAndRelease();
	// }
	// for (ProductBean productBean : productList) {
	// // 计算每一个原料用量 所需要的价格
	// for (FMaterial fMaterial : allFMList) {
	// if (fMaterial.getFid() == productBean.getFid()) {
	// // 价格转换
	// finalUnitPrice = UnitHandler.translate(fMaterial.getUnit(),
	// "克", fMaterial.getPrice());
	// // 计算原料成本：单价（元/克）*计划量（克）
	// price = finalUnitPrice * fMaterial.getPlan_amount()
	// + price;
	// }
	// }
	// // 设置产品的配方成本(单位：元/克)
	// productBean.setPrice(price);
	// price = 0f;
	// }
	// return pageBean;
	// }

	@Override
	public List<FormulaBean> getFormulaNameList(String keyword)
			throws CustomException {
		try {
			C3P0Utils.beginTransation();
			List<FormulaBean> list = dao.findFormulaNameList(keyword);
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
	public PageBean<PSeries> showPCPage(int currentPage, int currentCount,
			PSeries pSeries) throws CustomException {
		PageBean<PSeries> pageBean = new PageBean<PSeries>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setCurrentCount(currentCount);
		try {
			C3P0Utils.beginTransation();
			return dao.findPSAndPCList(pSeries, pageBean);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public int addOrUpdateSeriesAndCatogery(PSeries pSeries)
			throws CustomException {
		try {
			C3P0Utils.beginTransation();
			if (pSeries.getId() != null) { // id不为null 更新数据
				// 更新指定系列的类别
				dao.updatePS(pSeries);
				return 1;
			} else { // id为null 插入新纪录
				// 添加一条系列记录
				int id = dao.addSeries(pSeries);
				pSeries.setId(id);

				dao.addSeriesCatogery(pSeries);
				return 2;
			}
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public PSeries findSC(PSeries pSeries) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			return dao.findSeries(pSeries.getId());
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException(CustomException.SQL_EXCEPTION_SHOW);
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public List<Map<String, Object>> getAllSCZtree() throws CustomException {
		List<PSeries> psList = null;
		try {
			C3P0Utils.beginTransation();
			psList = dao.findAllPSAndPCList();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CustomException("获取系列和类别信息失败！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
		// 封装ztree数据的map列表
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		int id = 0;// 负责记录ztree的id
		int pid = 0;// 负责记录ztree的父id
		List<PCatogery> p_catogeris = null;
		// 循环所有的系列
		for (int i = 0; i < psList.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("id", ++id);
			map.put("pId", 0);
			map.put("name", psList.get(i).getS_name());
			map.put("sid", psList.get(i).getId());
			pid = id;
			mapList.add(map);// 添加一个系列节点

			p_catogeris = psList.get(i).getP_catogeris();
			// 循环所有的类别
			for (int j = 0; j < p_catogeris.size(); j++) {
				map = new HashMap<String, Object>();
				map.put("id", ++id);
				map.put("pId", pid);// 给pid指向系列的id，表示该类别在指定id系列的节点下
				map.put("name", p_catogeris.get(j).getName());
				map.put("cid", p_catogeris.get(j).getId());
				mapList.add(map);// 添加一个类别节点
			}
		}
		return mapList;
	}

	@Override
	public void deleteProduct(List<Map<String, Integer>> ids)
			throws CustomException {
		try {
			C3P0Utils.beginTransation();
			for (Map<String, Integer> map : ids) {
				dao.delete(map.get("id"));
			}
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("产品删除失败！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void deleteSC(List<Map<String, Integer>> ids) throws CustomException {
		try {
			C3P0Utils.beginTransation();
			for (Map<String, Integer> map : ids) {
				dao.deleteSC(map.get("id"));
			}
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("系列删除失败！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public List<PSeries> showSCList() throws CustomException {
		try {
			C3P0Utils.beginTransation();
			return dao.findAllPSAndPCList();
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("系列获取失败！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public List<String> showFormulaNumberList(String formula_number)
			throws CustomException {
		try {
			C3P0Utils.beginTransation();
			return dao.findFormulaNumberList(formula_number);
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("系列获取失败！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void parseXls(InputStream is) throws IOException, CustomException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

		try {
			// 判断供应商是否存在
			List<String> productNames = dao.findAllProductName();

			ProductExcelParseHelper helper = new ProductExcelParseHelper();
			for (Row row : hssfSheet) {
				System.out.println();
				for (Cell cell : row) {
					helper.parseExcel(cell.getRowIndex(), cell.getColumnIndex(),
							POIUtils.getHSSFCellStringValue((HSSFCell) cell), productNames);
				}
			}

			if (helper.getpList().size() == 0) {
				throw new CustomException("该表为空表！");
			}

			C3P0Utils.beginTransation();
			dao.addAll(helper.getpList());
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("数据库异常！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}

	@Override
	public void parseXlsx(InputStream is) throws IOException,
			CustomException {
		XSSFWorkbook xssfWorkbook = null;
		try {
			xssfWorkbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw new CustomException("excel文件无法解析，可能结构已被破坏！");
		}
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

		try {
			// 判断供应商是否存在
			List<String> productNames = dao.findAllProductName();

			ProductExcelParseHelper helper = new ProductExcelParseHelper();
			for (Row row : xssfSheet) {
				System.out.println();
				for (Cell cell : row) {
					helper.parseExcel(cell.getRowIndex(), cell.getColumnIndex(),
							POIUtils.getXSSFCellStringValue((XSSFCell) cell), productNames);
				}
			}

			if (helper.getpList().size() == 0) {
				throw new CustomException("该表为空表！");
			}

			C3P0Utils.beginTransation();
			dao.addAll(helper.getpList());
		} catch (SQLException e) {
			C3P0Utils.rollback();
			e.printStackTrace();
			throw new CustomException("数据库异常！");
		} finally {
			C3P0Utils.commitAndRelease();
		}
	}
}
