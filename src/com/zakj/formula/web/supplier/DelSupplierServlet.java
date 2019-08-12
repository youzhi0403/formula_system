package com.zakj.formula.web.supplier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.ISupplierService;
import com.zakj.formula.service.impl.SupplierServiceImpl;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class DelSupplierServlet
 */
public class DelSupplierServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		ISupplierService service = new SupplierServiceImpl();
		try {
			service.deleteSupplier(Integer.parseInt(id));
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, "删除成功"));
			return;
		} catch (CustomException e) {
			response.getWriter().write(StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
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
