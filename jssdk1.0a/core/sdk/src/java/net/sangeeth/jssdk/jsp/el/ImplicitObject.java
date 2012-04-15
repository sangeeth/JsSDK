package net.sangeeth.jssdk.jsp.el;

import java.util.Map;

public class ImplicitObject extends Token {
	public static final ImplicitObject PAGE_CONTEXT=new ImplicitObject("pageContext");
	public static final ImplicitObject PAGE_SCOPE=new ImplicitObject("pageScope");
	public static final ImplicitObject REQUEST_SCOPE=new ImplicitObject("requestScope");
	public static final ImplicitObject SESSION_SCOPE=new ImplicitObject("sessionScope");
	public static final ImplicitObject APPLICATION_SCOPE= new ImplicitObject("applicationScope");
	public static final ImplicitObject PARAM=new ImplicitObject("param");
	public static final ImplicitObject PARAM_VALUES= new ImplicitObject("paramValues");
	public static final ImplicitObject HEADER=new ImplicitObject("header");
	public static final ImplicitObject HEADER_VALUES=new ImplicitObject("headerValues");
	public static final ImplicitObject INIT_PARAM=new ImplicitObject("initParam");
	public static final ImplicitObject COOKIE=new ImplicitObject("cookie");

	private static final Map<String,ImplicitObject> cache = Token.getTokenMap(ImplicitObject.class);
	private ImplicitObject(String value) {
		super(value,TokenType.IMPLICIT_OBJECT);
	}
	public static ImplicitObject valueOf(String token) {
		return cache.get(token);
	}	
}
