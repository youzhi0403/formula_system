package com.zakj.formula.web.supplier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.ISupplierService;
import com.zakj.formula.service.impl.SupplierServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class AddSupplierServlet
 */
public class AddSupplierServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		SupplierBean supplier = BeanUtil.populate(SupplierBean.class,
				request.getParameterMap());
		ISupplierService service = new SupplierServiceImpl();
		try {
			service.addSupplier(supplier);
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, "添加成功"));
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
