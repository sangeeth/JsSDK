package net.sangeeth.jssdk.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class EnvFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
		if (req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest)req;
		    Cookie [] cookies = request.getCookies();
		    if (cookies!=null) {
		    	for(Cookie cookie:cookies) {
		    		if ("jssdkver".equals(cookie.getName())) {
		    			request.setAttribute("jssdkver",cookie.getValue());
		    		} else if ("scriptcompiled".equals(cookie.getName())) {
		    			request.setAttribute("scriptcompiled", "true");
		    		}
		    	}
		    }
		}
		chain.doFilter(req, res);
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
