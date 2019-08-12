package com.zakj.formula.web.material;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IMaterialService;
import com.zakj.formula.service.impl.MaterialServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.FileUtils;
import com.zakj.formula.utils.JsonUtils;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class MaterialServlet
 */
public class MaterialServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MaterialServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(201, "请输入action参数"));
		}
		IMaterialService service = new MaterialServiceImpl();
		switch (action) {
		case "showlist":
			showlist(request, response, service);
			break;
		case "add":
			addMaterial(request, response, service);
			break;
		case "update":
			updateMaterial(request, response, service);
			break;
		case "delete":
			deleteMaterial(request, response, service);
			break;
		case "import":
			importMaterial(request, response, service);
			break;
		case "template":
			downloadTemplate(request, response, service);
			break;
		default:
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(201, "无效的action参数！"));
			break;
		}
	}

	private void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response, IMaterialService service) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.getServletContext().getRealPath("原料导入模板.xls"));
			String header = request.getHeader("User-Agent");
			String filename = FileUtils.encodeDownloadFilename("原料导入模板.xls", header);
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

	private void importMaterial(HttpServletRequest request,
			HttpServletResponse response, IMaterialService service)
			throws IOException {
//		String realPath = this.getServletContext().getRealPath("/fileload");// 需要存放的真实路径
//		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//		if (!isMultipart)
//			return;
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
			if (item.isFormField()) {// 如果fileitem中封装的是普通输入项的数据
				// File fullFile = new File(item.getName());
				// 解决普通输入项的数据的中文乱码问题
				String name = item.getFieldName();
				String value = item.getString("UTF-8");
				System.out.println(name + "=" + value);
			} else {
				// 得到上传的文件名称，
				String filename = item.getName();
				System.out.println(filename);
				if (filename == null || filename.trim().equals("")) {
					continue;
				}
				// 注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：
				// c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
				// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
//				filename = filename.substring(filename.lastIndexOf("\\") + 1);
				// 获取item中的上传文件的输入流
				InputStream is = item.getInputStream();
				try {
					service.parseExcel(is);
					response.getWriter().write(
							StatusCodeUtil.getJsonStr(200, "导入成功！"));
				} catch (CustomException e) {
					e.printStackTrace();
					response.getWriter().write(
							StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
				}
			}
		}
	}

	private void deleteMaterial(HttpServletRequest request,
			HttpServletResponse response, IMaterialService service)
			throws IOException {
		String idStr = request.getParameter("id");
		if (idStr == null) {
			response.getWriter()
					.write(StatusCodeUtil.getJsonStr(201, "原料不存在！"));
		}
		try {
			service.deleteMaterial(Integer.parseInt(idStr));
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, "删除成功！"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (CustomException e) {
			e.printStackTrace();
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
		}
	}

	private void updateMaterial(HttpServletRequest request,
			HttpServletResponse response, IMaterialService service)
			throws IOException {
		try {
			MaterialBean materialBean = JsonUtils.toObject(MaterialBean.class,
					request);
			service.updateMaterial(materialBean);
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, "更新成功！"));
			return;
		} catch (CustomException e) {
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void addMaterial(HttpServletRequest request,
			HttpServletResponse response, IMaterialService service)
			throws IOException {
		try {
			MaterialBean materialBean = JsonUtils.toObject(MaterialBean.class,
					request);
			service.addMaterial(materialBean);
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, "添加成功"));
			return;
		} catch (CustomException e) {
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void showlist(HttpServletRequest request,
			HttpServletResponse response, IMaterialService service)
			throws IOException {
		PageBean<MaterialBean> pageBean;
		String currentPageStr = request.getParameter("currentPage");
		int currentPage = 1;
		if (currentPageStr != null) {
			currentPage = Integer.parseInt(currentPageStr);
			if (currentPage <= 0) {
				response.getWriter().write(
						StatusCodeUtil.getJsonStr(201, "当前页数必须大于零！"));
				return;
			}
		}
		try {
			MaterialBean materialBean = BeanUtil.populate(MaterialBean.class,
					request.getParameterMap());
			pageBean = service.findMaterialList(currentPage, 20, materialBean);
			response.getWriter()
					.write(StatusCodeUtil.getJsonStr(200, pageBean));
			return;
		} catch (CustomException e) {
			response.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
