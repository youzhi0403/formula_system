package com.zakj.formula.web.supplier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.ISupplierService;
import com.zakj.formula.service.impl.SupplierServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class UpdateSupplierServlet
 */
public class UpdateSupplierServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/Json; charset = UTF-8");
		SupplierBean bean = BeanUtil.populate(SupplierBean.class,
				request.getParameterMap());
		ISupplierService service = new SupplierServiceImpl();
		try {
			service.updateSupplier(bean);
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, "更新成功"));
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
