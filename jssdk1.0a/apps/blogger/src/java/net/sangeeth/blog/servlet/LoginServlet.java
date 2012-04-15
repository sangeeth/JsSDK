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
import net.sangeeth.blog.LoginService;

public class LoginServlet extends HttpServlet {

	private String ACTION_MANAGE = "/manage";
	private String VIEW_LOGIN = "/admin_login.jsp";
	LoginService loginService = new LoginService();
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
	    ACTION_MANAGE = servletConfig.getInitParameter("action_manage");
		VIEW_LOGIN = servletConfig.getInitParameter("view_login");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Blog postMaster = BlogManager.getBlog(request);
		request.setAttribute("previousPosts", postMaster.getMessages());
		
		String user = request.getParameter("user");
		String password = request.getParameter("password");
		
		try {
		    loginService.login(user, password);
			HttpSession session = request.getSession();
			session.setAttribute("userId", user);
			String contextPath = request.getContextPath();
			response.sendRedirect(contextPath + ACTION_MANAGE);
		} catch(Exception e){
			request.setAttribute("error", e.getMessage());
			String view = VIEW_LOGIN;
			RequestDispatcher dispatcher = request.getRequestDispatcher(view);
			if (dispatcher != null)
			   dispatcher.forward(request, response);			
		}
	}

}
