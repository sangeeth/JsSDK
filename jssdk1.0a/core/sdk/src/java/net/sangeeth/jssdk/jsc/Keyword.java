package net.sangeeth.jssdk.jsc;

import java.util.Map;
import static net.sangeeth.jssdk.jsc.TokenType.*;
public class Keyword extends Token {
	public static final Keyword BREAK=new Keyword("break");
	public static final Keyword CONTINUE=new Keyword("continue");
	public static final Keyword DELETE=new Keyword("delete");
	public static final Keyword ELSE=new Keyword("else");
	public static final Keyword FALSE= new Keyword("false",BOOLEAN_LITERAL);
	public static final Keyword FOR=new Keyword("for");
	public static final Keyword FUNCTION= new Keyword("function");
	public static final Keyword IF=new Keyword("if");
	public static final Keyword IN=new Keyword("in");
	public static final Keyword NEW=new Keyword("new");
	public static final Keyword NULL=new Keyword("null",NULL_LITERAL);
	public static final Keyword RETURN=new Keyword("return");
	public static final Keyword THIS=new Keyword("this");
    public static final Keyword TRUE=new Keyword("true",BOOLEAN_LITERAL);
	public static final Keyword VAR=new Keyword("var");
	public static final Keyword VOID=new Keyword("void");
	public static final Keyword WHILE=new Keyword("while");
	public static final Keyword WITH=new Keyword("with");
	public static final Keyword DO=new Keyword("do");
	public static final Keyword TYPEOF=new Keyword("typeof");
	public static final Keyword INSTANCEOF=new Keyword("instanceof");
	public static final Keyword SWITCH=new Keyword("switch");
	public static final Keyword CASE=new Keyword("case");
	public static final Keyword DEFAULT=new Keyword("default");
	public static final Keyword THROW=new Keyword("throw");
	public static final Keyword TRY=new Keyword("try");
	public static final Keyword CATCH=new Keyword("catch");
	public static final Keyword FINALLY=new Keyword("finally");

	private static final Map<String,Keyword> cache = Token.getTokenMap(Keyword.class);
	private Keyword(String value) {
		super(value,TokenType.KEYWORD);
	}
	private Keyword(String value,TokenType type) {
		super(value,type);
	}	
	public static Keyword valueOf(String token) {
		return cache.get(token);
	}	
}
