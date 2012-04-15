package net.sangeeth.jssdk.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class MutableHttpServletRequest implements HttpServletRequest {
	private String servletPath;
	private String contextPath;
	private String pathInfo;
	private String remoteAddr;
	private String remoteHost;
	private int remotePort;
	private File webappPath;
	private String protocol;
	private BufferedReader reader;
	private int localPort;
	private Map<String,String> parameterMap;
	private Locale locale;
	private String localName;
	private String localAddr;
	private ServletInputStream inputStream;
	private int contentLength;
	private String contentType;
	private String characterEncoding;
	private Map<String,Object> attributes;
	public MutableHttpServletRequest() {
		this.attributes = new TreeMap<String,Object>();
		this.parameterMap = new TreeMap<String,String>();
	}
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}

	public Enumeration getAttributeNames() {
		Vector<String> vector = new Vector<String>();
		vector.addAll(this.attributes.keySet());
		return vector.elements();
	}
	
	public String getCharacterEncoding() {
		return characterEncoding;
	}
	public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException {
		this.characterEncoding = characterEncoding;
	}
	public int getContentLength() {
		return contentLength;
	}
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public ServletInputStream getInputStream() throws IOException {
		return inputStream;
	}
	public void setInputStream(ServletInputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getLocalAddr() {
		return localAddr;
	}
	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public Locale getLocale() {
		return locale;
	}

	public Enumeration getLocales() {
		return null;
	}

	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public int getLocalPort() {
		return localPort;
	}
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	public void setParameter(String name,String value) {
		this.parameterMap.put(name, value);
	}
	
	public String getParameter(String name) {
		return this.parameterMap.get(name);
	}

	public void setParameterMap(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}
	public Map getParameterMap() {
		return this.parameterMap;
	}

	public Enumeration getParameterNames() {
		Vector<String> vector = new Vector<String>();
		vector.addAll(this.parameterMap.keySet());
		return vector.elements();
	}

	public String[] getParameterValues(String arg0) {
		return this.parameterMap.values().toArray(new String[0]);
	}

	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}
	public BufferedReader getReader() throws IOException, IllegalStateException {
		return this.reader;
	}

	public String getRealPath(String path) {
		File file = new File(webappPath,path);
		return file.getPath();
	}


	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public String getRemoteHost() {
		return remoteHost;
	}
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}
	public int getRemotePort() {
		return remotePort;
	}
	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}
	public RequestDispatcher getRequestDispatcher(String arg0) {
		return null;
	}

	public String getScheme() {
		return null;
	}

	public String getServerName() {
		return null;
	}

	public int getServerPort() {
		return 0;
	}

	public boolean isSecure() {
		return false;
	}

	public void removeAttribute(String key) {
		this.attributes.remove(key);
	}

	public void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}


	public String getAuthType() {
		return null;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public String getContextPath() {
		return this.contextPath;
	}

	public Cookie[] getCookies() {
		return null;
	}

	public long getDateHeader(String arg0) {
		return 0;
	}

	public String getHeader(String arg0) {
		return null;
	}

	public Enumeration getHeaderNames() {
		return null;
	}

	public Enumeration getHeaders(String arg0) {
		return null;
	}

	public int getIntHeader(String arg0) {
		return 0;
	}

	public String getMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPathInfo() {
		return pathInfo;
	}
	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;
	}
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getQueryString() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequestURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getServletPath() {
		return servletPath;
	}
	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpSession getSession(boolean arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
