package com.zakj.formula.web.privilege;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zakj.formula.bean.FormulaBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IPrivilegeService;
import com.zakj.formula.service.impl.PrivilegeServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class PrivilegeServlet
 */
public class PrivilegeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

	/*private void showlist(HttpServletRequest req, HttpServletResponse resp, 
			IPrivilegeService service) throws IOException {
		PrivilegeBean privilege = BeanUtil.populate(PrivilegeBean.class, req.getParameterMap());
		PageBean<PrivilegeBean> pageBean;
		String currentPageStr = req.getParameter("currentPage");
		int currentPage = 1;
		if(currentPageStr != null){
			currentPage = Integer.parseInt(currentPageStr);
		}
		try {
			pageBean = service.showPrivilegeList(currentPage, 20, privilege);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, pageBean));
		} catch (CustomException e) {
			resp.getWriter().write(StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
		
	}*/

}
