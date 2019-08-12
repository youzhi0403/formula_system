package com.zakj.formula.web.user;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.zakj.formula.bean.FormulaBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.bean.UserBean;
import com.zakj.formula.dao.user.IUserDao;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IRoleService;
import com.zakj.formula.service.IUserManagerService;
import com.zakj.formula.service.impl.RoleServiceImpl;
import com.zakj.formula.service.impl.UserManagerServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.JsonUtils;
import com.zakj.formula.utils.StatusCodeUtil;
import com.zakj.formula.utils.StringUtils;

/**
 * Servlet implementation class UserManagerServlet
 */
public class UserManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handlerAction(req, resp);
	}

	private void handlerAction(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String action = req.getParameter("action");
		if (action == null) {
			resp.getWriter().write(StatusCodeUtil.getJsonStr(201, "请输入action参数"));
			return;
		}
		IUserManagerService service = new UserManagerServiceImpl();
		switch (action) {
			case "showlist":
				showlist(req, resp, service);
				break;
			case "save":
				saveUser(req, resp, service);
				break;
			case "delete":
				deleteUser(req, resp, service);
				break;
			case "update":
				updateUser(req, resp, service);
				break;
	
			default:
				resp.getWriter().write(
						StatusCodeUtil.getJsonStr(201, "action参数错误！"));
				return;
		}
	}

	private void deleteUser(HttpServletRequest req, HttpServletResponse resp,
			IUserManagerService service) throws IOException {
		try {
			List<Map<String, Integer>> ids = JsonUtils.toObject(req.getReader(),
					new TypeToken<List<Map<String, Integer>>>() {}.getType());
			if(ids != null && ids.size() > 0){
				Integer id = ids.get(0).get("id");
				if(id == 1){
					resp.getWriter().write(StatusCodeUtil.getJsonStr(201, "无法删除超级管理员！"));
					return;
				}
				service.deleteUser(ids.get(0).get("id"));
				resp.getWriter().write(StatusCodeUtil.getJsonStr(200, "删除成功！"));
				return;
			}
		} catch (CustomException e) {
			e.printStackTrace();
			resp.getWriter().write(StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void updateUser(HttpServletRequest req, HttpServletResponse resp,
			IUserManagerService service) throws IOException {
		try {
			UserBean userBean = JsonUtils.toObject(UserBean.class, req);
			service.updateUser(userBean);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, "修改成功！"));
			return;
		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}

	}

	private void saveUser(HttpServletRequest req, HttpServletResponse resp,
			IUserManagerService service) throws IOException {
		try {
			UserBean userBean = JsonUtils.toObject(UserBean.class, req);
			service.addUser(userBean);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, "添加成功！"));
			return;
		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}

	}

	private void showlist(HttpServletRequest req, HttpServletResponse resp,
			IUserManagerService service) throws IOException {
		try {
			UserBean userBean = JsonUtils.toObject(UserBean.class, req);
			PageBean<UserBean> pageBean;
			String currentPageStr = req.getParameter("currentPage");
			int currentPage = 1;
			if (currentPageStr != null) {
				currentPage = Integer.parseInt(currentPageStr);
				if(currentPage <= 0){
					resp.getWriter().write(StatusCodeUtil.getJsonStr(201, "当前页数必须大于零！"));
					return;
				}
			}
			pageBean = service.showUserList(currentPage, 20, userBean);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, pageBean));
			return;
		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}

	}
}
