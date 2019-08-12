package com.zakj.formula.web.formula;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zakj.formula.bean.MaterialBean;
import com.zakj.formula.dao.formula.IFormulaDao;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IFormulaService;
import com.zakj.formula.service.impl.FormulaServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class ShowMaterialNameListServlet
 */
public class ShowMaterialNameListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IFormulaService service = new FormulaServiceImpl();
		try {
			List<MaterialBean> list = service.getMaterialNameList(new MaterialBean());
			response.getWriter().write(StatusCodeUtil.getJsonStr(200, list));
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
