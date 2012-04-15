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

public class HomeServlet extends HttpServlet {
	
	private String VIEW = "/index.jsp";
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		VIEW = servletConfig.getInitParameter("view");
	}

	public void doGet(HttpServletRequest request,
			              HttpServletResponse response)
	        throws IOException, ServletException {
		Blog blog = BlogManager.getBlog(request);
		request.setAttribute("previousPosts", blog.getMessages());
		RequestDispatcher dispatcher = request.getRequestDispatcher(VIEW);
		if (dispatcher != null)
		   dispatcher.forward(request, response);
	}
}
