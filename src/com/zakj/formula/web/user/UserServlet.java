package com.zakj.formula.web.user;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.zakj.formula.bean.UserBean;
import com.zakj.formula.exception.CustomException;
import com.zakj.formula.service.ILoginService;
import com.zakj.formula.service.impl.LoginServiceImpl;
import com.zakj.formula.utils.BeanUtil;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet implementation class LoginServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			response.getWriter().write(StatusCodeUtil.getJsonStr(201, "请输入action参数"));
		}
		switch (action) {
		case "LOGIN":
			login(request, response);
			break;
		case "EXIT":
			logout(request, response);
			break;
		default:
			response.getWriter().write(StatusCodeUtil.getJsonStr(201, "错误的action参数！"));
			break;
		}
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserBean user = BeanUtil.populate(UserBean.class,request.getParameterMap());
		HttpSession session = request.getSession();
		ILoginService service = new LoginServiceImpl();
		try {
			Cookie userNameCookie = new Cookie("userName", URLEncoder.encode(service.login(user.getAccount(), user.getPassword(),session), "utf-8"));
			//如果不设置这个，cookie的path会默认保存为servlet的访问路径，如果我们修改过web.xml中servlet的路径，那么在客户端获取cookie的时候，就会拿不到cookie
			userNameCookie.setPath("/");
			//登录，然后返回用户的用户名并存入cookie中，返回给客户端
			response.addCookie(userNameCookie);
			System.out.println(URLEncoder.encode(service.login(user.getAccount(), user.getPassword(),session)));
			//登录，然后返回用户的用户名并存入cookie中，返回给客户端
			Cookie userPrivilegeCookie = new Cookie("userPrivilege", new Gson().toJson(service.getUserPrivilege((List<String>) request.getSession().getAttribute("privilege"))));
			userPrivilegeCookie.setPath("/");
			System.out.println(new Gson().toJson(service.getUserPrivilege((List<String>) request.getSession().getAttribute("privilege"))));
			response.addCookie(userPrivilegeCookie);
		} catch (CustomException e) {
			response.getWriter().write(StatusCodeUtil.getJsonStr(e.getCode(), e.getMsg()));
			return;
		}
		response.getWriter().write(StatusCodeUtil.getJsonStr(200,"登录成功！"));
		return;
	}

	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		Cookie userNameCookie = new Cookie("userName", null);
		userNameCookie.setMaxAge(0);
		response.addCookie(userNameCookie);
		Cookie userPrivilegeCookie = new Cookie("userPrivilege", null);
		userPrivilegeCookie.setMaxAge(0);
		response.addCookie(userPrivilegeCookie);
		response.getWriter().write(StatusCodeUtil.getJsonStr(200, "退出成功"));
		return;
	}
}
