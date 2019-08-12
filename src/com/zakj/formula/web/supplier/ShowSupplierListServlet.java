package com.zakj.formula.web.supplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mchange.v2.lang.StringUtils;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.SupplierBean;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.ISupplierService;
import com.zakj.formula.service.impl.SupplierServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class LikeSupplierServlet
 */
public class ShowSupplierListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ISupplierService service = new SupplierServiceImpl();
		SupplierBean bean = BeanUtil.populate(SupplierBean.class,
				request.getParameterMap());
		PageBean<SupplierBean> pageBean;
		String currentPageStr = request.getParameter("currentPage");
		int currentPage = 1;
		if(currentPageStr != null){
			currentPage = Integer.parseInt(currentPageStr);
			if(currentPage <= 0){
				response.getWriter().write(StatusCodeUtil.getJsonStr(201, "错误的页码！"));
				return;
			}
		}
		try {
			pageBean = service.findSupplierLikeList(currentPage, 20,bean);
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, pageBean));
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
