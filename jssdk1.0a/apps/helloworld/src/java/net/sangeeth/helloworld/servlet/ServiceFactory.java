package net.sangeeth.helloworld.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ServiceFactory {
	public static <A extends Object> A getService(Class<A> type,HttpServletRequest request) {
		String typeName = type.getClass().getName();
		HttpSession session = request.getSession(true);
		Object service = session.getAttribute(typeName);
		if (service==null) {
			try {
				service = type.getClass().newInstance();
				session.setAttribute(typeName, service);
			} catch (Exception e) {
				// Not handling the exception appropriately to keep the implementation simple
			}
		}
		return (A)service;
	}
}
