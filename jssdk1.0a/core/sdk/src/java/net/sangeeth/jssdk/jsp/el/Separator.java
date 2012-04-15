package net.sangeeth.jssdk.jsp.el;

import java.util.Map;

public class Separator extends Token {
	public static final Separator SEP_OPEN_PARAMETER_BLOCK = new Separator("(");
	public static final Separator SEP_CLOSE_PARAMETER_BLOCK = new Separator(")");
	public static final Separator SEP_OPEN_ARRAY_BLOCK = new Separator("[");
	public static final Separator SEP_CLOSE_ARRAY_BLOCK = new Separator("]");
	public static final Separator SEP_COMMA = new Separator(",");
	public static final Separator SEP_PERIOD = new Separator(".");
	
	private static final Map<String,Separator> cache = Token.getTokenMap(Separator.class);
	private Separator(String value) {
		super(value, TokenType.SEPARATOR);
		// TODO Auto-generated constructor stub
	}
	public static Separator valueOf(String token) {
		return cache.get(token);
	}
	
}
