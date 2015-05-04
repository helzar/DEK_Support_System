package com.nulp.dss.web.filter;

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

public class LoginationFilter implements Filter {

	
	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		HttpSession session =  ((HttpServletRequest)request).getSession(true);
		if (request instanceof HttpServletRequest) {
			String url = ((HttpServletRequest) request).getRequestURL().toString();
		
			
			if (url.contains("singin.xhtml") ^ session.getAttribute("USER") != null){
				chain.doFilter(request, response);
			}
			else if (url.contains("singin.xhtml") && session.getAttribute("USER") != null){
				((HttpServletResponse)response).sendRedirect("index.xhtml");
			}
			else{
				((HttpServletResponse)response).sendRedirect("singin.xhtml");
			}
		}
		else{
			((HttpServletResponse)response).sendRedirect("singin.xhtml");
		}
	}


	public void init(FilterConfig fConfig) throws ServletException {}
	
}
