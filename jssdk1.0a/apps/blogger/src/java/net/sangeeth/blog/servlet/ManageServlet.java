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
import net.sangeeth.blog.Message;

public class ManageServlet extends HttpServlet {
	
	private String VIEW_MANAGE = "/admin_manage.jsp";
	private String VIEW_EDIT = "/admin_edit_post.jsp";
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);

		VIEW_MANAGE = servletConfig.getInitParameter("view_manage");
		VIEW_EDIT = servletConfig.getInitParameter("view_edit");
	}

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		String messageId = request.getParameter("messageId");
		Blog blog = BlogManager.getBlog(request);
		
		String view = VIEW_MANAGE;
		
		if (messageId!=null&&"delete".equals(action) ) {
			blog.removeMessage(messageId);
		} else if (messageId!=null&&"edit".equals(action)) {
			view = VIEW_EDIT;
			Message message = blog.getMessage(messageId);
			if (message!=null) {
				request.setAttribute("message", message);
			}
		} else if ("new".equals(action)) {
			view = VIEW_EDIT;
		} else if ("publish".equals(action)) {
			String title = request.getParameter("title");
			String body = request.getParameter("body");
			if (title!=null && body!=null) {
				HttpSession session = request.getSession();
				String userId = (String)session.getAttribute("userId");
				blog.updateMessage(userId, messageId, title, body);
			}
		} 
		
		request.setAttribute("previousPosts", blog.getMessages());
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(view);
		if (dispatcher != null)
		   dispatcher.forward(request, response);			
	}

}
