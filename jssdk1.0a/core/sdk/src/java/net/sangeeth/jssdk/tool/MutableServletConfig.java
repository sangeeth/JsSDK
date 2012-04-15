package net.sangeeth.jssdk.tool;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class MutableServletConfig implements ServletConfig {

	private Map<String,String> initParameter;
	public MutableServletConfig() {
		this.initParameter = new TreeMap<String,String>();
	}
	public void setInitParameter(String name,String value) {
		this.initParameter.put(name,value);
	}
	public String getInitParameter(String name) {
		return this.initParameter.get(name);
	}

	public Enumeration getInitParameterNames() {
		Vector<String> vector = new Vector<String>();
		vector.addAll(initParameter.keySet());
		return vector.elements();
	}

	public ServletContext getServletContext() {
		return null;
	}

	public String getServletName() {
		return null;
	}

}
