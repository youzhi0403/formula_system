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

/**
 * Servlet Filter implementation class EncodeFilter
 */
public class EncodeFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		resp.setHeader( "Pragma", "no-cache" );   
		resp.setDateHeader("Expires", 0);   
		resp.addHeader( "Cache-Control", "no-cache" );//浏览器和缓存服务器都不应该缓存页面信息
//		
//		if (req.getRequestURI().contains(req.getContextPath()+"/api/")){
//			req.setCharacterEncoding("utf-8");
//			response.setContentType("application/Json; charset = UTF-8");
//		} else {
			req.setCharacterEncoding("utf-8");
			if(!req.getRequestURI().contains(req.getContextPath()+"/css")){
				response.setContentType("text/html; charset = UTF-8");
			}
			
//		}
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}


}
