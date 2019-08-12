package com.zakj.formula.web.product;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.reflect.TypeToken;
import com.zakj.formula.bean.PSeries;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.ProductBean;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IProductService;
import com.zakj.formula.service.impl.ProductServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.FileUtils;
import com.zakj.formula.utils.JsonUtils;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");
		if (action == null) {
			resp.getWriter().write(StatusCodeUtil.getJsonStr(201, "请输入参数"));
		}
		IProductService service = new ProductServiceImpl();
		switch (action) {
		case "showPSAndPC": // show出所有的系列和各个系列的所有类别
			showPCPage(req, resp, service);
			break;
		case "showSCList": // show出所有的系列和各个系列的所有类别
			showSCList(req, resp, service);
			break;
		case "addSC": // 添加或更新系列及其类别
			addOrUpdateSeriesAndCatogery(req, resp, service);
			break;
		case "updateSC": // 添加或更新系列及其类别
			addOrUpdateSeriesAndCatogery(req, resp, service);
			break;
		case "deleteSC": // 删除系列及其类别
			deleteSC(req, resp, service);
			break;
		case "showSC": // 查找指定系列的所有类别
			showCategoryBySeries(req, resp, service);
			break;
		case "update": // 更新产品信息
			updateProduct(req, resp, service);
			break;
		case "showSCztree": // 展示所有系列和类别并勾选指定产品所属的系列和类别
			showSCztree(req, resp, service);
			break;
		case "delete": // 删除产品
			deleteProduct(req, resp, service);
			break;
		case "add": // 添加产品
			addProduct(req, resp, service);
			break;
		case "showlist": // 展示产品列表
			showProductList(req, resp, service);
			break;
		case "showFormulaNumberList": // 获取配方编号列表
			showFormulaNumberList(req, resp, service);
			break;
		case "import": // 产品导入
			importProduct(req, resp, service);
			break;
		case "template": // 产品导入
			downloadTemplate(req, resp, service);
			break;
		default:
			resp.getWriter()
					.write(StatusCodeUtil.getJsonStr(201, "错误的参数，请重试！"));
			return;
		}
	}

	private void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response, IProductService service) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.getServletContext().getRealPath(
					"产品导入模板.xls"));
			String header = request.getHeader("User-Agent");
			String filename = FileUtils.encodeDownloadFilename("产品导入模板.xls",
					header);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename);

			OutputStream os = response.getOutputStream();
			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = fis.read(bytes)) != -1) {
				os.write(bytes, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void importProduct(HttpServletRequest request,
			HttpServletResponse response, IProductService service)
			throws IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// upload.setFileSizeMax(1024 * 1024);// the max size of the upload file
		List<FileItem> items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(201, "上传文件失败！"));
			e.printStackTrace();
		}

		for (FileItem item : items) {
			if (!item.isFormField()) {// 如果fileitem中封装的是普通输入项的数据
				// 得到上传的文件名称，
				try {
					String filename = item.getName();
					// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
					// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
					// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					// filename = filename.substring(filename.lastIndexOf("\\")
					// +
					// 1);
					// 获取item中的上传文件的输入流
					switch (filename.substring(filename.lastIndexOf(".") + 1)) {
					case "xls":
						service.parseXls(item.getInputStream());
						response.getWriter().write(
								StatusCodeUtil.getJsonStr(200, "导入成功！"));
						break;
					case "xlsx":
						service.parseXlsx(item.getInputStream());
						response.getWriter().write(
								StatusCodeUtil.getJsonStr(200, "导入成功！"));
						break;
					default:
						response.getWriter().write(
								StatusCodeUtil.getJsonStr(201, "导入的不是一个excel文件！"));
						return;
					}
				} catch (CustomException e) {
					response.getWriter().write(
							StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
					e.printStackTrace();
				}
			} else {
				response.getWriter().write(
						StatusCodeUtil.getJsonStr(201, "请选择一个上传文件！"));
			}
		}
	}

	private void showFormulaNumberList(HttpServletRequest req,
			HttpServletResponse resp, IProductService service)
			throws IOException {
		String formula_number = req.getParameter("formula_number");
		List<String> numbers;
		try {
			numbers = service.showFormulaNumberList(formula_number);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, numbers));
		} catch (CustomException e) {
			e.printStackTrace();
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void showSCList(HttpServletRequest req, HttpServletResponse resp,
			IProductService service) throws IOException {
		try {
			List<PSeries> scList = service.showSCList();
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, scList));
		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void showProductList(HttpServletRequest req,
			HttpServletResponse resp, IProductService service)
			throws IOException {
		PageBean<ProductBean> pageBean;
		String currentPageStr = req.getParameter("currentPage");
		int currentPage = 1;
		if (currentPageStr != null) {
			currentPage = Integer.parseInt(currentPageStr);
			if (currentPage <= 0) {
				resp.getWriter().write(
						StatusCodeUtil.getJsonStr(201, "当前页数必须大于零！"));
				return;
			}
		}
		try {
			ProductBean product = BeanUtil.populate(ProductBean.class,
					req.getParameterMap());
			pageBean = service.showProductList(currentPage, 20, product);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, pageBean));
			return;
		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void addProduct(HttpServletRequest req, HttpServletResponse resp,
			IProductService service) throws IOException {
		try {
			ProductBean product = JsonUtils.toObject(ProductBean.class, req);
			service.addProduct(product);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, "添加成功"));
			return;
		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}

	}

	private void deleteProduct(HttpServletRequest req,
			HttpServletResponse resp, IProductService service)
			throws IOException {
		try {
			List<Map<String, Integer>> ids = JsonUtils.toObject(
					req.getReader(),
					new TypeToken<List<Map<String, Integer>>>() {
					}.getType());
			if (ids != null && ids.size() > 0) {
				service.deleteProduct(ids);
				resp.getWriter().write(StatusCodeUtil.getJsonStr(200, "删除成功！"));
			}
		} catch (CustomException e) {
			e.printStackTrace();
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void showSCztree(HttpServletRequest req,
			HttpServletResponse response, IProductService service)
			throws IOException {
		try {
			List<Map<String, Object>> scZtree = service.getAllSCZtree();
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, scZtree));
			return;
		} catch (CustomException e) {
			e.printStackTrace();
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void updateProduct(HttpServletRequest request,
			HttpServletResponse response, IProductService service)
			throws IOException {
		try {
			ProductBean product = JsonUtils
					.toObject(ProductBean.class, request);
			service.updateProduct(product);
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, "更新成功"));
			return;
		} catch (CustomException e) {
			e.printStackTrace();
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}

	}

	private void showCategoryBySeries(HttpServletRequest req,
			HttpServletResponse resp, IProductService service)
			throws IOException {
		try {
			PSeries pSeries = JsonUtils.toObject(PSeries.class, req);
			pSeries = service.findSC(pSeries);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, pSeries));
			return;
		} catch (CustomException e) {
			e.printStackTrace();
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}

	}

	private void deleteSC(HttpServletRequest req, HttpServletResponse resp,
			IProductService service) throws IOException {
		try {
			List<Map<String, Integer>> sids = JsonUtils.toObject(
					req.getReader(),
					new TypeToken<List<Map<String, Integer>>>() {
					}.getType());
			if (sids != null && sids.size() > 0) {
				service.deleteSC(sids);
				resp.getWriter().write(StatusCodeUtil.getJsonStr(200, "删除成功！"));
				return;
			}
		} catch (NumberFormatException | CustomException e) {
			e.printStackTrace();
			if (e instanceof CustomException) {
				resp.getWriter().write(
						StatusCodeUtil.getJsonStr(
								((CustomException) e).getCode(),
								((CustomException) e).getMsg()));
			}
			if (e instanceof NumberFormatException) {
				resp.getWriter().write(
						StatusCodeUtil.getJsonStr(201, "sid应该是一个int类型数据。"));
			}
		}
	}

	private void addOrUpdateSeriesAndCatogery(HttpServletRequest req,
			HttpServletResponse resp, IProductService service)
			throws IOException {
		try {
			PSeries pSeries = JsonUtils.toObject(PSeries.class, req);
			int rtn = service.addOrUpdateSeriesAndCatogery(pSeries);
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(200, rtn == 1 ? "更新成功" : "添加成功"));
			return;
		} catch (CustomException e) {
			e.printStackTrace();
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void showPCPage(HttpServletRequest req, HttpServletResponse resp,
			IProductService service) throws IOException {
		PageBean<PSeries> pageBean;
		String currentPageStr = req.getParameter("currentPage");
		int currentPage = 1;
		if (currentPageStr != null) {
			currentPage = Integer.parseInt(currentPageStr);
			if (currentPage <= 0) {
				resp.getWriter().write(
						StatusCodeUtil.getJsonStr(201, "当前页数必须大于零！"));
				return;
			}
		}
		try {
			PSeries pSeries = BeanUtil.populate(PSeries.class,
					req.getParameterMap());
			System.out.println("系列类:"+pSeries.toString());
			pageBean = service.showPCPage(currentPage, 20, pSeries);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, pageBean));
			return;
		} catch (CustomException e) {
			e.printStackTrace();
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

}
