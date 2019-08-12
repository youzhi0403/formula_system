package com.zakj.formula.web.product;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zakj.formula.bean.FormulaBean;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IProductService;
import com.zakj.formula.service.impl.ProductServiceImpl;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class ShowFormulaNameListServlet
 */
public class ShowFormulaNameListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/Json; charset = UTF-8");
		IProductService service = new ProductServiceImpl();
		try {
			List<FormulaBean> list = service.getFormulaNameList(request.getParameter("keyword"));
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, list));
			return;
		} catch (CustomException e) {
			response.getWriter().write(StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
