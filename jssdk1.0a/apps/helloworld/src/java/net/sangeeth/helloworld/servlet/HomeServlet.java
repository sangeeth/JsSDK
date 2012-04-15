package net.sangeeth.helloworld.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sangeeth.helloworld.UserManagementService;

@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet {
	
	private String VIEW = "/index.jsp";
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		VIEW = servletConfig.getInitParameter("view");
	}

	public void doGet(HttpServletRequest request,
			              HttpServletResponse response)
	        throws IOException, ServletException {
		UserManagementService service = ServiceFactory.getService(UserManagementService.class,
				                                                  request);
		request.setAttribute("userList", service.getUsers());
		RequestDispatcher dispatcher = request.getRequestDispatcher(VIEW);
		if (dispatcher != null)
		   dispatcher.forward(request, response);
	}
}
