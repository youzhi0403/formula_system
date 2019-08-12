package com.zakj.formula.web.formula;

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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.google.gson.reflect.TypeToken;
import com.zakj.formula.bean.FormulaDesc;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IFormulaService;
import com.zakj.formula.service.impl.FormulaServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.ExcelExportHelper;
import com.zakj.formula.utils.FileUtils;
import com.zakj.formula.utils.JsonUtils;
import com.zakj.formula.utils.StatusCodeUtil;
import com.zakj.formula.utils.StringUtils;

/**
 * Servlet implementation class Formula
 */
public class FormulaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(201, "请输入action参数"));// 转到404页面
			return;
		}
		IFormulaService service = new FormulaServiceImpl();
		switch (action) {
		case "showdetails": // 配方详情展示
			showFormulaDetails(request, response, service);
			break;
		case "import": // 配方导入
			importExcel(request, response, service);
			break;
		case "showlist": // 配方列表展示
			showFormulaList(request, response, service);
			break;
		case "output": // 配方导出
			outputFormulaDesc(request, response, service);
			break;
		case "update":  // 配方详情更新
			updateFormulaDesc(request, response, service);
			break;
		case "delete":  // 配方删除
			deleteFormulaDesc(request, response, service);
			break;
		case "template":  // 配方模板下载
			downloadTemplate(request, response, service);
			break;
			
		default:
			response.getWriter().write(StatusCodeUtil.getJsonStr(201, "action参数错误！"));
			return;
		}
	}

	private void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response, IFormulaService service) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.getServletContext().getRealPath("配方导入模板.xls"));
			String header = request.getHeader("User-Agent");
			String filename = FileUtils.encodeDownloadFilename("配方导入模板.xls", header);
			response.setHeader("Content-Disposition", "attachment;filename="+filename);  
			
			OutputStream os = response.getOutputStream();
			byte[] bytes = new byte[1024];
			int len = 0;
  			while((len = fis.read(bytes)) != -1){
				os.write(bytes, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void deleteFormulaDesc(HttpServletRequest request,
			HttpServletResponse response, IFormulaService service) throws IOException {
		try {
			List<Map<String, Integer>> ids = JsonUtils.toObject(request.getReader(),
					new TypeToken<List<Map<String, Integer>>>() {}.getType());
			if(ids != null && ids.size() > 0){
				service.deleteFormulaDesc(ids.get(0).get("id"));
				response.getWriter().write(StatusCodeUtil.getJsonStr(200, "删除成功！"));
			}
		} catch (CustomException e) {
			e.printStackTrace();
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	/**
	 * 导出配方详情到excel文件
	 * 
	 * @param request
	 * @param response
	 * @param service
	 * @throws IOException
	 */
	private void outputFormulaDesc(HttpServletRequest request,
			HttpServletResponse response, IFormulaService service)
			throws IOException {
		String format = request.getParameter("format");
		String id = request.getParameter("id");
		int fid = 0;
		if (StringUtils.isEmpty(id, true)) {
			response.getWriter().write(StatusCodeUtil.getJsonStr(201, "找不到该配方！"));
			return;
		} else {
			fid = Integer.parseInt(id);
		}
		try {
			switch (format) {
			case "xls":
				HSSFWorkbook hssfWorkbook = service.getFormulaDescXlsExcel(fid, this.getServletContext().getRealPath("demo.xls"));
				String header = request.getHeader("User-Agent");
//				System.out.println("ExcelExportHelper.FILE_NAME  :  "+ExcelExportHelper.FILE_NAME);
				String filename = FileUtils.encodeDownloadFilename(ExcelExportHelper.FILE_NAME, header);
				response.setHeader("Content-Disposition", "attachment;filename="+filename);  
				hssfWorkbook.write(response.getOutputStream());
				break;
			case "xlsx":
				
				break;

			default:
				response.getWriter().write(StatusCodeUtil.getJsonStr(201, "选择导入的excel格式错误"));
				return;
			}
		} catch (CustomException e) {
			e.printStackTrace();
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}
	

	/*
	 * author:cwj
	 */
	private void updateFormulaDesc(HttpServletRequest request,
			HttpServletResponse response, IFormulaService service) throws IOException {
		//先获得数据
		List<String> list = new ArrayList<String>();
		try {
			FormulaDesc formulaDesc = JsonUtils.toObject(FormulaDesc.class, request);
			list = service.updateFormulaDesc(formulaDesc);
			if(list.size()>0){
				response.getWriter().write(StatusCodeUtil.getJsonStr(201, list));
			}else{
				try {
					response.getWriter().write(StatusCodeUtil.getJsonStr(200, "更新成功"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			/*System.out.println(formulaDesc.toString());*/
			
		} catch (CustomException e) {
			e.printStackTrace();
			response.getWriter().write(StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}

	}


	private void showFormulaList(HttpServletRequest request,
			HttpServletResponse response, IFormulaService service)
			throws IOException {
		try {
			FormulaDesc formulaDesc = BeanUtil.populate(FormulaDesc.class,
					request.getParameterMap());
			PageBean<FormulaDesc> pageBean;
			String currentPageStr = request.getParameter("currentPage");
			int currentPage = 1;
			if (currentPageStr != null) {
				currentPage = Integer.parseInt(currentPageStr);
				if(currentPage <= 0){
					response.getWriter().write(StatusCodeUtil.getJsonStr(201, "当前页数必须大于零！"));
					return;
				}
			}
			pageBean = service.getFormulaDescList(currentPage, 20, formulaDesc);
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, pageBean));
			return;
		} catch (CustomException e) {
			e.printStackTrace();
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void importExcel(HttpServletRequest request,
			HttpServletResponse response, IFormulaService service)
			throws IOException {
		try {
			InputStream is = request.getInputStream();
			StringBuffer sb = new StringBuffer();
			int count = 0;
			while (true) {
				int a = is.read();
				sb.append((char) a);
				if (a == '\r')
					count++;
				if (count == 4) {
					is.read();
					break;
				}
			}
			String title = sb.toString();
			String[] lines = title.split("\r\n");
			String headers1[] = lines[1].split(";");
			String fileName = headers1[2].split("=")[1].replace("\"", "");
			// 解决文件名中文乱码
			fileName = new String(fileName.getBytes("iso-8859-1"),
					request.getCharacterEncoding());
			switch (fileName.substring(fileName.lastIndexOf(".") + 1)) {
			case "xls":
				service.parseXls(is);
				response.getWriter().write(
						StatusCodeUtil.getJsonStr(200, "导入成功！"));
				break;
			case "xlsx":
				service.parseXlsx(is);
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
			return;
		}
	}

	private void showFormulaDetails(HttpServletRequest request,
			HttpServletResponse response, IFormulaService service)
			throws IOException {
		try {
			// FormulaDesc formulaDesc = BeanUtil.populate(FormulaDesc.class,
			// request.getParameterMap());
			FormulaDesc formulaDesc = JsonUtils.toObject(FormulaDesc.class,request);
			if (formulaDesc.getId() == null) {
				response.getWriter().write(StatusCodeUtil.getJsonStr(201, "请输入id参数"));// 转到404页面
				return;
			}
			formulaDesc = service.getFormulaDescById(formulaDesc.getId());
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(200, formulaDesc));
			return;
		} catch (CustomException e) {
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
