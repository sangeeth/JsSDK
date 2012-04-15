package net.sangeeth.jssdk.jsp.el;

import java.util.Map;
import static net.sangeeth.jssdk.jsp.el.TokenType.*;
public class Keyword extends Token {
	public static final Keyword FALSE= new Keyword("false",BOOLEAN_LITERAL);
	public static final Keyword NULL=new Keyword("null",NULL_LITERAL);
    public static final Keyword TRUE=new Keyword("true",BOOLEAN_LITERAL);

	private static final Map<String,Keyword> cache = Token.getTokenMap(Keyword.class);
	private Keyword(String value,TokenType type) {
		super(value,type);
	}	
	public static Keyword valueOf(String token) {
		return cache.get(token);
	}	
}
