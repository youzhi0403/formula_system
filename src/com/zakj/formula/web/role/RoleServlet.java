package com.zakj.formula.web.role;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zakj.formula.bean.FormulaBean;
import com.zakj.formula.bean.PageBean;
import com.zakj.formula.bean.PrivilegeBean;
import com.zakj.formula.bean.RoleBean;
import com.zakj.formula.bean.UserBean;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.IPrivilegeService;
import com.zakj.formula.service.IRoleService;
import com.zakj.formula.service.impl.LoginServiceImpl;
import com.zakj.formula.service.impl.PrivilegeServiceImpl;
import com.zakj.formula.service.impl.RoleServiceImpl;
import com.zakj.formula.service.impl.UserManagerServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.JsonUtils;
import com.zakj.formula.utils.StatusCodeUtil;
import com.zakj.formula.utils.StringUtils;

/**
 * Servlet implementation class RoleServlet
 */
public class RoleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");
		if (action == null) {
			resp.getWriter().write(StatusCodeUtil.getJsonStr(201, "请输入action参数"));
		}
		IRoleService service = new RoleServiceImpl();
		switch (action) {
			case "showlist":
				showlist(req, resp, service);
				break;
			case "save":
				saveRole(req, resp, service);
				break;
			case "delete":
				deleteRole(req, resp, service);
				break;
			case "update":
				updateRole(req, resp, service);
				break;
			case "showPrivilegeTree":
				showPrivilegeZtreeList(req, resp, service);
				break;
			case "showNameList":
				showRoleNameList(req, resp, service);
				break;
			case "savePrivileges":
				savePrivilegeZtree(req, resp, service);
				break;
	
			default:
				break;
		}
	}

	private void deleteRole(HttpServletRequest req, HttpServletResponse resp,
			IRoleService service) throws IOException {
		try {
			List<Map<String, Integer>> ids = JsonUtils.toObject(req.getReader(),
					new TypeToken<List<Map<String, Integer>>>() {}.getType());
			if(ids != null && ids.size() > 0){
				service.deleteRole(ids.get(0).get("id"));
				resp.getWriter().write(StatusCodeUtil.getJsonStr(200, "删除成功！"));
			}
		} catch (CustomException e) {
			e.printStackTrace();
			resp.getWriter().write(StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void savePrivilegeZtree(HttpServletRequest req,
			HttpServletResponse resp, IRoleService service) throws IOException {
		try {
			RoleBean roleBean = JsonUtils.toObject(RoleBean.class, req);
			service.savePrivilege(roleBean);
			//更新session
			UserBean user = (UserBean) req.getSession().getAttribute("user");
			//获取所有的权限code码，并保存更新到session中
			List<String> privilegeCodes = new UserManagerServiceImpl().findPrivilegeUrlByUID(user.getId());
			req.getSession().setAttribute("privilege", privilegeCodes);
			//根据session中保存的权限code码，获取用户的权限map对象，并通过cookie返回给客户端，覆盖之前的cookie
			Map<String, Boolean> userPrivilege = new LoginServiceImpl().getUserPrivilege(privilegeCodes);
			Cookie userPrivilegeCookie = new Cookie("userPrivilege", new Gson().toJson(userPrivilege));
			userPrivilegeCookie.setPath("/");
			resp.addCookie(userPrivilegeCookie);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, "权限设置成功！"));
			return;
		} catch (CustomException e) {
			resp.getWriter().write(StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void updateRole(HttpServletRequest req, HttpServletResponse resp,
			IRoleService service) throws IOException {
		try {
			RoleBean roleBean = JsonUtils.toObject(RoleBean.class, req);
			service.updateRole(roleBean);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, "修改成功！"));
		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void saveRole(HttpServletRequest req, HttpServletResponse resp,
			IRoleService service) throws IOException {
		try {
			RoleBean roleBean = JsonUtils.toObject(RoleBean.class, req);
			service.addRole(roleBean);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, "添加成功！"));
		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void showRoleNameList(HttpServletRequest req,
			HttpServletResponse resp, IRoleService service) throws IOException {
		try {
			List<RoleBean> roleNameList = service.showRoleNameList();
			if (roleNameList != null) {
				resp.getWriter().write(
						StatusCodeUtil.getJsonStr(200, roleNameList));
			} else {
				resp.getWriter().write(
						StatusCodeUtil.getJsonStr(201, "获取数据库数据失败!"));
			}

		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

	private void showPrivilegeZtreeList(HttpServletRequest req,
			HttpServletResponse resp, IRoleService service) throws IOException {
		try {
			String rid = req.getParameter("rid");
			ArrayList<Map<String, Object>> privilegeList = service
					.showPrivilegeZtreeList(new Integer(rid));
			if (privilegeList != null) {
				resp.getWriter().write(
						StatusCodeUtil.getJsonStr(200, privilegeList));
			} else {
				resp.getWriter().write(
						StatusCodeUtil.getJsonStr(201, "获取权限列表失败！"));
			}
		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}

	}

	private void showlist(HttpServletRequest req, HttpServletResponse resp,
			IRoleService service) throws IOException {
		RoleBean role = BeanUtil
				.populate(RoleBean.class, req.getParameterMap());
		PageBean<RoleBean> pageBean;
		String currentPageStr = req.getParameter("currentPage");
		int currentPage = 1;
		if (currentPageStr != null) {
			currentPage = Integer.parseInt(currentPageStr);
			if(currentPage <= 0){
				resp.getWriter().write(StatusCodeUtil.getJsonStr(201, "当前页数必须大于零！"));
				return;
			}
		}
		try {
			pageBean = service.showRoleList(currentPage, 20, role);
			resp.getWriter().write(StatusCodeUtil.getJsonStr(200, pageBean));
		} catch (CustomException e) {
			resp.getWriter().write(
					StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
	}

}
