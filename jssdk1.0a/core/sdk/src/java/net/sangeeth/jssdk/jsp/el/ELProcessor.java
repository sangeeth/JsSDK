package net.sangeeth.jssdk.jsp.el;

import java.util.Set;
import java.util.TreeSet;

public class ELProcessor {
	private Set<String> symbols;
	private ELTokenizer tokenizer;
	private StringBuffer exp;
	
	public ELProcessor() {
	}
	public String process(String el) {
		exp = new StringBuffer();
		
		tokenizer = new ELTokenizer(el);
		tokenizer.nextToken();
		
		expression();
		   
        return exp.toString();
	}
	
	public ELResult parse(String el) {
		exp = new StringBuffer();
		
		symbols = new TreeSet<String>();
		
		tokenizer = new ELTokenizer(el);
		tokenizer.nextToken();
		
		expression();
		
		ELResult result = new ELResult();
		result.setProcessedText(exp.toString());
		result.setSymbols(symbols);
		   
        return result;
	}
	private void error(String message) {
		System.out.println(message);
	}
	private void appendToken(Token token) {
		exp.append(token.getValue());
		tokenizer.nextToken();
	}
	/*
	 Expression ::=  BooleanLiteral
               | NumericLiteral
               | NullLiteral
               | Expression1 [ ? Expression : Expression ]
	 */
	private boolean expression() {
		boolean accepted = false;
		Token token = tokenizer.currentToken();
		if (token==null) return false;
		
		TokenType tokenType = token.getType();
		
		if (tokenType == TokenType.BOOLEAN_LITERAL
			|| tokenType == TokenType.NULL_LITERAL
			|| tokenType == TokenType.NUMERIC_LITERAL) {
			appendToken(token);
			accepted = true;
		} else if (expression1()) {
			token = tokenizer.currentToken();
			if (token == Operator.CONDITION_QUESTION) {
				appendToken(token);
				if (expression()) {
					token = tokenizer.currentToken();
					if (token == Operator.CONDITION_COLON) {
						appendToken(token);
						if (expression()) {
							accepted = true;
						} else {
							error("Expression expected");
						}
					}
				} else {
					error("Expression expected");
				}
			}
			accepted = true;
		}
		
		return accepted;
	}
    /*
	Expression1 ::= UnaryOp Expression1
               | '(' Expression ')' [ Expression1Part ]
               | Identifier [ Expression2Part ]
               | ImplicitObject [ Expression1Part ]
               | StringLiteral [ Expression2Part ]
     */	
	private boolean expression1() {
		boolean accepted = false;
		Token token = tokenizer.currentToken();
		if (token==null) return false;
		
		TokenType tokenType = token.getType();

		if (unaryOperator()) {
			exp.append(" ");
			if (expression1()) {
				accepted = true;
			} else {
				error("expression expected");
			}
		} else if (token == Separator.SEP_OPEN_PARAMETER_BLOCK) {
			appendToken(token);
			if (expression()) {
				token = tokenizer.currentToken();
				if (token == Separator.SEP_CLOSE_PARAMETER_BLOCK) {
					appendToken(token);
					expression1Part();
					accepted = true;
				} else {
					error("')' expected");
				}
			} else {
				error("expression expected");
			}
		} else if (tokenType == TokenType.IDENTIFIER) {
			symbols.add(token.getValue());
//			exp.append(ImplicitObject.PAGE_CONTEXT);
//			exp.append(Separator.SEP_PERIOD);
			appendToken(token);
			expression2Part();
			accepted = true;
		} else if (tokenType == TokenType.IMPLICIT_OBJECT) {
			appendToken(token);
			expression1Part();
			accepted = true;
		} else if (tokenType == TokenType.STRING_LITERAL) {
			appendToken(token);
			expression2Part();
			accepted = true;
		}		
		return accepted;
	}
	/*
	Expression1Part ::= . Identifier [ Expression2Part ]
                   | [ Expression ] [ Expression2Part ]
	 */
	private boolean expression1Part() {
		boolean accepted = false;
		Token token = tokenizer.currentToken();
		if (token==null) return false;
		if (token == Separator.SEP_PERIOD) {
			appendToken(token);
			token = tokenizer.currentToken();
			if (token.getType() == TokenType.IDENTIFIER) {
				appendToken(token);
				expression2Part();
				accepted = true;
			}
		} else if (token == Separator.SEP_OPEN_ARRAY_BLOCK) {
			appendToken(token);
			if (expression()) {
				token = tokenizer.currentToken();
				if (token == Separator.SEP_CLOSE_ARRAY_BLOCK) {
					appendToken(token);
					expression2Part();
					accepted = true;
				}
			}
		}
		return accepted;
	}
    /*
	Expression2Part ::=  Expression1Part
                         | BinaryOp Expression
     */	
	private boolean expression2Part() {
		boolean accepted = false;
		Token token = tokenizer.currentToken();
		if (token==null) return false;
		
		if (expression1Part()) {
			accepted = true;
		} else if (binaryOperator()) {
			if (expression()) {
				accepted = true;
			} else {
				error("Expression expected");
			}
		}
		return accepted;
	}

	private boolean binaryOperator() {
		boolean accepted = false;
		Token token = tokenizer.currentToken();
		if (token==null) return false;
		if (token.getType() == TokenType.BINARY_OPERATOR) {
			appendToken(token);
			accepted = true;
		}
		return accepted;
	}
	private boolean unaryOperator() {
		boolean accepted = false;
		Token token = tokenizer.currentToken();
		if (token==null) return false;
		if (token.getType() == TokenType.UNARY_OPERATOR) {
			appendToken(token);
			accepted = true;
		}
		return accepted;
	}
}
