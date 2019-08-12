package com.zakj.formula.web.supplier;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IProductService;
import com.zakj.formula.service.ISupplierService;
import com.zakj.formula.service.impl.ProductServiceImpl;
import com.zakj.formula.service.impl.SupplierServiceImpl;
import com.zakj.formula.utils.FileUtils;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class SupplierServlet
 */
public class SupplierServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		if (action == null) {
			resp.getWriter().write(StatusCodeUtil.getJsonStr(201, "请输入参数"));
		}
		ISupplierService service = new SupplierServiceImpl();
		switch (action) {
		case "import": // show出所有的系列和各个系列的所有类别
			importSupplier(req, resp, service);
			break;
		case "template": // show出所有的系列和各个系列的所有类别
			downloadTemplate(req, resp, service);
			break;
		default:
			resp.getWriter()
					.write(StatusCodeUtil.getJsonStr(201, "错误的参数，请重试！"));
			return;
		}
	}

	private void downloadTemplate(HttpServletRequest request,
			HttpServletResponse response, ISupplierService service) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.getServletContext().getRealPath("供应商导入模板.xls"));
			String header = request.getHeader("User-Agent");
			String filename = FileUtils.encodeDownloadFilename("供应商导入模板.xls", header);
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

	private void importSupplier(HttpServletRequest request,
			HttpServletResponse response, ISupplierService service) throws IOException {
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
				// filename = filename.substring(filename.lastIndexOf("\\") +
				// 1);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
