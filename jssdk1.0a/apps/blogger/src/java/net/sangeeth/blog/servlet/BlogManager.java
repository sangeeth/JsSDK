package net.sangeeth.blog.servlet;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sangeeth.blog.Blog;
import net.sangeeth.blog.Message;

public class BlogManager {
	private static final String BLOG_KEY=Blog.class.getName();
	public static Blog getBlog(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Blog blog = (Blog)session.getAttribute(BLOG_KEY);
		if (blog==null) {
			blog = new Blog();
			session.setAttribute(BLOG_KEY, blog);
		}
		return blog;
	}
}
