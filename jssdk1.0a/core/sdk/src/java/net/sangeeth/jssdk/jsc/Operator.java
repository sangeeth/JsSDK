package net.sangeeth.jssdk.jsc;

import java.util.Map;
import static net.sangeeth.jssdk.jsc.TokenType.*;
public final class Operator extends Token {
	// Unary Operators
	// ! ? : + - * / % ++ --
	public static final Operator NOT = new Operator("!",UNARY_OPERATOR);
	public static final Operator CONDITION_QUESTION = new Operator("?",UNARY_OPERATOR);
	public static final Operator CONDITION_COLON = new Operator(":",UNARY_OPERATOR);
	public static final Operator ADDITION = new Operator("+",ARITHEMATIC_OPERATOR);
	public static final Operator SUBTRACTION = new Operator("-",ARITHEMATIC_OPERATOR);
	public static final Operator MULTIPLICATION = new Operator("*",ARITHEMATIC_OPERATOR);
	public static final Operator DIVISION = new Operator("/",ARITHEMATIC_OPERATOR);
	public static final Operator MODULO = new Operator("%",ARITHEMATIC_OPERATOR);
	public static final Operator INCREMENT = new Operator("++",POSTFIX_OPERATOR);
	public static final Operator DECREMENT = new Operator("--",POSTFIX_OPERATOR);
	// Bitwise Operator
	// ~ & | ^
	public static final Operator BITWISE_COMPLEMENT = new Operator("~",BITWISE_OPERATOR);
	public static final Operator BITWISE_AND = new Operator("&",BITWISE_OPERATOR);
	public static final Operator BITWISE_OR = new Operator("|",BITWISE_OPERATOR);
	public static final Operator BITWISE_XOR = new Operator("^",BITWISE_OPERATOR);
	// Relational Operators
	// > < != == <= >=  && ||
	public static final Operator EQUALITY = new Operator("==",RELATIONAL_OPERATOR);
	public static final Operator STRICTLY_EQUAL = new Operator("===",RELATIONAL_OPERATOR);
	public static final Operator STRICTLY_NOT_EQUAL = new Operator("!==",RELATIONAL_OPERATOR);
	public static final Operator LESSER_THAN = new Operator("<",RELATIONAL_OPERATOR);
	public static final Operator GREATER_THAN = new Operator(">",RELATIONAL_OPERATOR);
	public static final Operator LESSER_THAN_OR_EQUAL = new Operator("<=",RELATIONAL_OPERATOR);
	public static final Operator GREATER_THAN_OR_EQUAL = new Operator(">=",RELATIONAL_OPERATOR);
	public static final Operator NOT_EQUAL = new Operator("!=",RELATIONAL_OPERATOR);
	public static final Operator OR = new Operator("||",RELATIONAL_OPERATOR);
	public static final Operator AND = new Operator("&&",RELATIONAL_OPERATOR);
	//Assignment Operators 
	//	= += -= *= /= &= |= ^= %= <<= >>= >>>=
	public static final Operator EQUAL = new Operator("=",ASSIGNMENT_OPERATOR);
	public static final Operator MULTIPLY_EQUAL = new Operator("*=",ASSIGNMENT_OPERATOR);
	public static final Operator DIVIDE_EQUAL = new Operator("/=",ASSIGNMENT_OPERATOR);
	public static final Operator MODULO_EQUAL = new Operator("%=",ASSIGNMENT_OPERATOR);
	public static final Operator ADD_EQUAL = new Operator("+=",ASSIGNMENT_OPERATOR);
	public static final Operator SUBTRACT_EQUAL = new Operator("-=",ASSIGNMENT_OPERATOR);
	public static final Operator SHIFT_LEFT_EQUAL = new Operator("<<=",ASSIGNMENT_OPERATOR);
	public static final Operator SHIFT_RIGHT_EQUAL = new Operator(">>=",ASSIGNMENT_OPERATOR);
	public static final Operator SHIFT_DOUBLE_RIGHT_EQUAL = new Operator(">>>=",ASSIGNMENT_OPERATOR);
	public static final Operator BITWISE_AND_EQUAL = new Operator("&=",ASSIGNMENT_OPERATOR);
	public static final Operator BITWISE_XOR_EQUAL = new Operator("^=",ASSIGNMENT_OPERATOR);
	public static final Operator BITWISE_OR_EQUAL = new Operator("|=",ASSIGNMENT_OPERATOR);
	//Shift Operators
	//<< >> >>>
	public static final Operator SHIFT_LEFT = new Operator("<<",SHIFT_OPERATOR);
	public static final Operator SHIFT_RIGHT = new Operator(">>",SHIFT_OPERATOR);
	public static final Operator SHIFT_DOUBLE_RIGHT = new Operator(">>>",SHIFT_OPERATOR);
	
	private static Map<String,Operator> cache = getTokenMap(Operator.class);
	private Operator(String value, TokenType type) {
		super(value, type);
	}
	public static Operator valueOf(String token) {
		return cache.get(token);
	}
}
