package net.sangeeth.jssdk.jsc;

import java.util.Map;

public class Separator extends Token {
	public static final Separator OPEN_PARAMETER_BLOCK = new Separator("(");
	public static final Separator CLOSE_PARAMETER_BLOCK = new Separator(")");
	public static final Separator OPEN_BLOCK = new Separator("{");
	public static final Separator CLOSE_BLOCK = new Separator("}");
	public static final Separator OPEN_ARRAY_BLOCK = new Separator("[");
	public static final Separator CLOSE_ARRAY_BLOCK = new Separator("]");
	public static final Separator COMMA = new Separator(",");
	public static final Separator PERIOD = new Separator(".");
	public static final Separator SEMICOLON = new Separator(";");
	
	private static final Map<String,Separator> cache = Token.getTokenMap(Separator.class);
	private Separator(String value) {
		super(value, TokenType.SEPARATOR);
		// TODO Auto-generated constructor stub
	}
	public static Separator valueOf(String token) {
		return cache.get(token);
	}
	
}
