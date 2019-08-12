package com.zakj.formula.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zakj.formula.bean.UserBean;
import com.zakj.formula.utils.StatusCodeUtil;

/**
 * Servlet Filter implementation class UserLoginPrivilegeFilter
 */
public class UserLoginPrivilegeFilter implements Filter {
       
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		//以下请求不做拦截
		if(req.getRequestURI().contains(req.getContextPath()+"/image")
				||req.getRequestURI().contains(req.getContextPath()+"/js")
				||req.getRequestURI().contains(req.getContextPath()+"/css")){
			chain.doFilter(request, response);
			return;
		}
		HttpSession session = req.getSession();
		UserBean userBean = (UserBean) session.getAttribute("user");
		if(userBean == null){
			if((req.getContextPath()+"/login.html").equals(req.getRequestURI())
					||req.getRequestURI().contains(req.getContextPath()+"/api/user/login")){
				chain.doFilter(request, response);
				return;
			} else {
				res.sendRedirect(req.getContextPath()+"/login.html");
			}
			if(req.getRequestURI().contains(req.getContextPath()+"/api")){
				res.getWriter().write(StatusCodeUtil.getJsonStr(-1, "用户未登录或登录已过期！"));
			}
			return;
		} else {
			if((req.getContextPath()+"/login.html").equals(req.getRequestURI())){
				res.sendRedirect(req.getContextPath()+"/welcome.html");
				return;
			}
//			if(req.getRequestURI().contains(req.getContextPath()+"/api/user/login")){
//				res.getWriter().write(StatusCodeUtil.getJsonStr(201, "用户已登录！"));
//			}
			String action = req.getParameter("action");
			String uri = req.getRequestURI();
			if(action != null){
				uri = req.getRequestURI()+"?action="+req.getParameter("action");
			}
			boolean checked = userBean.checkPrivilegeByUrl(uri);
			if(!checked){
				response.getWriter().write(StatusCodeUtil.getJsonStr(201, "权限不足，无法访问！"));
				return;
			}
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
