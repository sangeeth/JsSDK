package net.sangeeth.jssdk.jsp.el;

import java.util.Map;
import static net.sangeeth.jssdk.jsp.el.TokenType.*;
public final class Operator extends Token {
	public static final Operator NOT = new Operator("!",UNARY_OPERATOR);
	public static final Operator NOT_TEXT = new Operator("not",UNARY_OPERATOR);
	public static final Operator EMPTY = new Operator("empty",UNARY_OPERATOR);
	
	public static final Operator CONDITION_QUESTION = new Operator("?",UNARY_OPERATOR);
	public static final Operator CONDITION_COLON = new Operator(":",UNARY_OPERATOR);
	
	public static final Operator ADDITION = new Operator("+",BINARY_OPERATOR);
	public static final Operator SUBTRACTION = new Operator("-",BINARY_OPERATOR);
	public static final Operator MULTIPLICATION = new Operator("*",BINARY_OPERATOR);
	public static final Operator DIVISION = new Operator("/",BINARY_OPERATOR);
	public static final Operator DIVISION_TEXT = new Operator("div",BINARY_OPERATOR);
	public static final Operator MODULO = new Operator("%",BINARY_OPERATOR);
	public static final Operator MODULO_TEXT = new Operator("mod",BINARY_OPERATOR);
	public static final Operator EQUALITY = new Operator("==",BINARY_OPERATOR);
	public static final Operator EQUALITY_TEXT = new Operator("eq",BINARY_OPERATOR);
	public static final Operator LESSER_THAN = new Operator("<",BINARY_OPERATOR);
	public static final Operator LESSER_THAN_TEXT = new Operator("lt",BINARY_OPERATOR);
	public static final Operator GREATER_THAN = new Operator(">",BINARY_OPERATOR);
	public static final Operator GREATER_THAN_TEXT = new Operator("gt",BINARY_OPERATOR);
	public static final Operator LESSER_THAN_OR_EQUAL = new Operator("<=",BINARY_OPERATOR);
	public static final Operator LESSER_THAN_OR_EQUAL_TEXT = new Operator("le",BINARY_OPERATOR);
	public static final Operator GREATER_THAN_OR_EQUAL = new Operator(">=",BINARY_OPERATOR);
	public static final Operator GREATER_THAN_OR_EQUAL_TEXT = new Operator("ge",BINARY_OPERATOR);
	public static final Operator NOT_EQUAL = new Operator("!=",BINARY_OPERATOR);
	public static final Operator NOT_EQUAL_TEXT = new Operator("ne",BINARY_OPERATOR);
	public static final Operator OR = new Operator("||",BINARY_OPERATOR);
	public static final Operator OR_TEXT = new Operator("or",BINARY_OPERATOR);
	public static final Operator AND = new Operator("&&",BINARY_OPERATOR);
	public static final Operator AND_TEXT = new Operator("and",BINARY_OPERATOR);

	private static Map<String,Operator> cache = getTokenMap(Operator.class);
	private Operator(String value, TokenType type) {
		super(value, type);
	}
	public static Operator valueOf(String token) {
		return cache.get(token);
	}
}
