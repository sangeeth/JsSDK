package net.sangeeth.blog.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sangeeth.blog.Blog;

public class AdminServlet extends HttpServlet {
	
	private String VIEW_LOGIN = "/admin_login.jsp";
	private String VIEW_MANAGE = "/admin_manage.jsp";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Blog postMaster = BlogManager.getBlog(request);
		request.setAttribute("previousPosts", postMaster.getMessages());
		
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		String view = VIEW_LOGIN;
		if (userId!=null) {
			view= VIEW_MANAGE;
		} 
		RequestDispatcher dispatcher = request.getRequestDispatcher(view);
		if (dispatcher != null)
		   dispatcher.forward(request, response);
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		
		VIEW_LOGIN = servletConfig.getInitParameter("view_login");
		VIEW_MANAGE = servletConfig.getInitParameter("view_manage");
	}
}
