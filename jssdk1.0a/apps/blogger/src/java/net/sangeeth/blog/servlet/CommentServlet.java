package net.sangeeth.blog.servlet;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sangeeth.blog.Blog;
import net.sangeeth.blog.Comment;
import net.sangeeth.blog.Message;

public class CommentServlet extends HttpServlet {
	
	private String VIEW = "/view_comments.jsp";
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		
		VIEW = servletConfig.getInitParameter("view");
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String messageId = request.getParameter("messageId");
		Blog blog = BlogManager.getBlog(request);
		
		Message message = blog.getMessage(messageId);
		request.setAttribute("message", message);
		
		String view = VIEW;
		request.setAttribute("previousPosts", blog.getMessages());

		RequestDispatcher dispatcher = request.getRequestDispatcher(view);
		if (dispatcher != null)
		   dispatcher.forward(request, response);			
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String messageId = request.getParameter("messageId");
		Blog blog = BlogManager.getBlog(request);
		request.setAttribute("previousPosts", blog.getMessages());
		
		if (messageId!=null) {
			String author = request.getParameter("author");
			String email = request.getParameter("email");
			String website = request.getParameter("website");
			String body = request.getParameter("body");
			blog.addComment(messageId, author, email, website, body);
		}
		doGet(request,response);
	}	
}
