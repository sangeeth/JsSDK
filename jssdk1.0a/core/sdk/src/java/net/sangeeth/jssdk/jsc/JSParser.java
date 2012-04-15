package net.sangeeth.jssdk.jsc;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JSParser implements JSParserListener {
	
	private List<JSParserListener> listeners;
	
	private DirectiveRef directiveRef;
	
	private JSLexer lexer;
	private Token lastConsumedToken;
	private Token lookAhead;
	private boolean warningEnabled;
	
	
	private static class SymbolBuilder extends JSParserAdapter {
		int depth;

		@Override
		public void started() {
			depth =0;
		}

		public void beginBlock() {
			for(int i=0;i<depth;i++) {
				System.out.print('\t');
			}
			System.out.println("{");
			depth++;		
		}

		public void consumedToken(Token token) {
			if (token.getType()==TokenType.IDENTIFIER) {
				for(int i=0;i<depth;i++) {
					System.out.print('\t');
				}
				System.out.printf("%s\n",token);	
			}
		}
		public void endBlock() {
			depth--;
			for(int i=0;i<depth;i++) {
				System.out.print('\t');
			}
			System.out.println("}");
		}

	}
	
	private SymbolBuilder symbolBuilder;
	
	public JSParser() {
		//this.symbolBuilder = new SymbolBuilder();
		this.listeners = new ArrayList<JSParserListener>();
		//this.addParserListener(symbolBuilder);
	}
	public void addParserListener(JSParserListener listener) {
		this.listeners.add(listener);
	}
	public void removedParserListener(JSParserListener listener) {
		this.listeners.remove(listener);
	}
	public void consumedToken(Token token) {
		this.lastConsumedToken = token;
		for(JSParserListener listener:listeners) {
			listener.consumedToken(token);
		}
	}

	public void definedSymbol(Symbol symbol) {
		// TODO Auto-generated method stub
		
	}

	public void declaredDirective(DirectiveRef directiveRef) {
		for(JSParserListener listener:listeners) {
			listener.declaredDirective(directiveRef);
		}		
	}
	public void finished(boolean success) {
		// TODO Auto-generated method stub
		
	}

	public void started() {
		// TODO Auto-generated method stub
		
	}

	public void beginBlock() {
		for(JSParserListener listener:listeners) {
			listener.beginBlock();
		}		
	}
	public void endBlock() {
		for(JSParserListener listener:listeners) {
			listener.endBlock();
		}		
	}
	
	public void beginScope() {
		// TODO Auto-generated method stub
		
	}
	public void endScope() {
		// TODO Auto-generated method stub
		
	}
	public boolean isWarningEnabled() {
		return warningEnabled;
	}

	public void setWarningEnabled(boolean warningEnabled) {
		this.warningEnabled = warningEnabled;
	}

	public boolean parse(File file) throws Exception {
		lexer = new JSLexer(new FileReader(file));
		
		lookAhead = lexer.nextToken();
		program();
		
		return (this.lookAhead==null);
	}
	private void showStackTrace() {
		if (warningEnabled) {
			Exception e = new Exception();
			StackTraceElement [] stacktrace = e.getStackTrace();
			if (stacktrace!=null && stacktrace.length>2) {
				for(int i=2;i<stacktrace.length;i++) {
					System.err.print(stacktrace[i].getMethodName() + "<-");
				}
				System.err.println();
			}
		}
	}
	private void consume(Token token) {
		if (token==null) {
			consumedToken(this.lookAhead);
			lookAhead = lexer.nextToken();
		} else {
			consumedToken(token);
		}
	}
	private void consume() {
		consume(null);
	}

	private boolean accept(TokenType tokenType) {
		boolean accepted = false;
		if (this.lookAhead.getType() == tokenType) {
			consume();
			accepted = true;
		}
		return accepted;
	}
	private boolean expect(TokenType tokenType) {
		boolean success = accept(tokenType);
		if (!success) {
			showStackTrace();
			if (warningEnabled) {
				System.err.println("Err: Expected " + tokenType + " found " + this.lookAhead.getType() + " at " + this.lexer.getLineNumber());
			}
		}
		return success;
	}	
	private boolean accept(Token token) {
		boolean accepted = false;
		if (this.lookAhead == token) {
			consume();
			accepted = true;
		}
		return accepted;
	}
	private boolean expect(Token token) {
		boolean success = accept(token);
		if (!success) {
			showStackTrace();
			if (warningEnabled)
				System.err.println("Err: Expected " + token + " but found " + this.lookAhead + " at " + this.lexer.getLineNumber());
		}
		return success;
	}
	
//	Program ::= [SourceElements]
	private boolean program() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (sourceElements()) {
			accepted = true;
		}
		
		return accepted;
	}
//	SourceElements ::= SourceElement [ SourceElements ]
	private boolean sourceElements() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		while(sourceElement()) {
			accepted = true;
		}
		
		return accepted;
	}

//	SourceElement ::= FunctionDeclaration
//               	  Statement
	private boolean sourceElement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (functionDeclaration()) {
			accepted = true;
		} else if (statement()) {
			accepted = true;
		}
		
		return accepted;
	}	
//	FunctionDeclaration ::= function Identifier FunctionDeclarationPart
	private boolean functionDeclaration() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.FUNCTION)) {
			if (expect(TokenType.IDENTIFIER)) {
				if (functionDeclarationPart()) {
					accepted = true;
				}
			}
		}
		
		return accepted;
	}
//	FunctionDeclarationPart ::=
//		( [ FormalParameterList ] )  { [FunctionBody] }

	private boolean functionDeclarationPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.OPEN_PARAMETER_BLOCK)) {
			formalParameterList();
			if (accept(Separator.CLOSE_PARAMETER_BLOCK)){
				if (accept(Separator.OPEN_BLOCK)) {
					
					//fire begin scope
					beginBlock();
					
					functionBody();
					if (accept(Separator.CLOSE_BLOCK)) {
						
						//fire end scope
						endBlock();
						
						accepted = true;
					}
				}
			}
		}
		
		return accepted;
	}
//	FunctionBody ::= 
//		SourceElements
	private boolean functionBody() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (sourceElements()) {
			accepted = true;
		}
		
		return accepted;
	}
	
//	FormalParameterList ::= 
//		Identifier [ , FormalParamterList ]
	private boolean formalParameterList() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.IDENTIFIER)) {
			if (accept(Separator.COMMA)) {
				formalParameterList();
			}
			accepted = true;
		}
		
		return accepted;
	}
//	Statement ::=
//  DirectiveStatement
//  LabellableStatement
//  Identifier IdentifierStatementPart ;
//	  ExpressionStatement		
	private boolean statement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted; 
		
		if (directiveStatement()) {
			accepted = true;
		} else if (labellableStatement()) {
			accepted = true;
		} else if(accept(TokenType.IDENTIFIER)) {
			identifierStatementPart();
			if (!accept(Separator.SEMICOLON)) {
				consume(Separator.SEMICOLON);
			}
			accepted = true;
		} else if (expressionStatement()) {
			accepted = true;
		}
		
		return accepted;
	}
//	DirectiveStatement ::=
//		Directive ( [DirectiveArgumentList] ) ;
	private boolean directiveStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		if (accept(TokenType.DIRECTIVE)) {
			directiveRef = new DirectiveRef(this.lastConsumedToken.getValue());
			if (expect(Separator.OPEN_PARAMETER_BLOCK)) {
				directiveArgumentList();
				if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
					if(!accept(Separator.SEMICOLON)){
						consume(Separator.SEMICOLON);
					}
					accepted = true;
					
					declaredDirective(directiveRef);
				}
			}
		}
		return accepted;
	}
// DirectiveArgumentList ::=
//		StringLiteral [, DirectiveArgumentList ] 	
	private boolean directiveArgumentList() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.STRING_LITERAL)) {
			directiveRef.getArgs().add(this.lastConsumedToken.getValue());
			accepted = true;
			if (accept(Separator.COMMA)) {
				if (!directiveArgumentList()) {
					accepted = false;
				}
			}
		}
		return accepted;
	}
	
//	LabellableStatement::=  
//		Block
//	    VariableStatement
//	    EmptyStatement
//	    IfStatement
//	    IterationStatement
//	    ContinueStatement
//	    BreakStatement
//	    ReturnStatement
//	    WithStatement
//	    SwitchStatement
//	    ThrowStatement
//	    TryStatement	
	private boolean labellableStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (block()
			||variableStatement()
			||emptyStatement()
			||ifStatement()
			||iterationStatement()
			||continueStatement()
			||breakStatement()
			||returnStatement()
			||withStatement()
			||switchStatement()
			||throwStatement()
			||tryStatement()) {
			accepted = true;
		} 
		return accepted;
	}
//	Block ::=  
//		{ [ StatementList ]  }
	private boolean block() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.OPEN_BLOCK)) {
			//fire begin scope
			beginBlock();

			statementList();
			if (accept(Separator.CLOSE_BLOCK)) {
				
				//fire begin scope
				endBlock();

				accepted = true;
			}
		}
		
		return accepted;
	}
//	StatementList ::= 
//		Statement [ StatementList ]	
	private boolean statementList() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (statement()) {
			statementList();
			accepted = true;
		}
		
		return accepted;
	}
//	VariableStatement ::=  
//		var VariableDeclarationList ;	
	private boolean variableStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.VAR)) {
			if (variableDeclarationList()) {
				if(!accept(Separator.SEMICOLON)){
					consume(Separator.SEMICOLON);
				}
				//TODO warn that "semicolon is missing.
				accepted = true;
			}
		}
		
		return accepted;
	}
//	VariableDeclarationList ::=
//		VariableDeclaration [ , VariableDeclarationList ]	
	private boolean variableDeclarationList() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (variableDeclaration()) {
			if (accept(Separator.COMMA)) {
				variableDeclarationList();
			}
			accepted = true;
		}
		
		return accepted;
	}
//	VariableDeclaration ::= 
//		Identifier [ Initialiser ]
	private boolean variableDeclaration() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.IDENTIFIER)) {
			initializer();
			accepted = true;
		}
		
		return accepted;
	}
	
//	Initialiser ::=  
//		= Expression
	private boolean initializer() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Operator.EQUAL)) {
			//TODO: Call Expression 
			if (expression()) {
				accepted =true;
			}
		}
		
		return accepted;
	}

//	EmptyStatement ::= 
//		;
	private boolean emptyStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.SEMICOLON)) {
			accepted = true; 
		}
		
		return accepted;
	}
//	IfStatement ::=  
//		if  ( Expression ) Statement [ else Statement ]
	private boolean ifStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.IF)) {
			if (accept(Separator.OPEN_PARAMETER_BLOCK)) {
				if (expression()) {
					if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
						if (statement()) {
							if (accept(Keyword.ELSE)) {
								statement();
							}
							accepted = true;
						}
					}
				}
			}
		}
		
		return accepted;
	}
	
//	IterationStatement ::=  
//		do Statement while ( Expression )  ;
//		while ( Expression ) Statement
//		for ( ForStatementPart
	private boolean iterationStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.DO)) {
			//fire begin scope
			beginBlock();

			if (statement()) {
				if (expect(Keyword.WHILE)) {
					if (expect(Separator.OPEN_PARAMETER_BLOCK)) {
						if (expression()) {
							if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
								if(!accept(Separator.SEMICOLON)){
									consume(Separator.SEMICOLON);
								}
								accepted = true;
							}
						}
					}
				}
			}
			
			//fire end scope
			endBlock();
		} else if (accept(Keyword.WHILE)) {
			//fire begin scope
			beginBlock();

			if (expect(Separator.OPEN_PARAMETER_BLOCK)) {
				if (expression()) {
					if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
						if (statement()) {
							accepted = true;
						}
					}
				}
			}
			
			//fire end scope
			endBlock();			
		} else if (accept(Keyword.FOR)) {
			if (expect(Separator.OPEN_PARAMETER_BLOCK)) {
				//fire begin scope
				beginBlock();

				if (forStatementPart()) {
					accepted = true;
				}
				
				//fire begin scope
				endBlock();
			}
		}
		
		return accepted;
	}
//	ForStatementPart ::=
//	  var VariableDeclaration ForStatementVarPart
//	  ForStatementRest
//	  Identifier ForStatementIdentifierPart
//	  NoIdentifierComputableExpressionPart ExpressionRest ForStatementRest	
	private boolean forStatementPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.VAR)) {
			if (variableDeclaration()) {
				if (forStatementVarPart()) {
					accepted = true;
				}
			}
		} else if (forStatementRest()) {
			accepted = true;
		} else if (accept(TokenType.IDENTIFIER)) {
			if (forStatementIdentifierPart()) {
				accepted = true;
			}
		}		
		return accepted;
	}
//	ForStatementVarPart ::=
//	  ForStatementAllRest
//	  , VariableDeclarationList ForStatementRest
	private boolean forStatementVarPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (forStatementAllRest()) {
			accepted = true;
		} else if (accept(Separator.COMMA)) {
			if (variableDeclarationList()) {
				if (forStatementRest()) {
					accepted = true;
				}
			}
		}  
		
		return accepted;
	}

//	ForStatementRest ::=  
//	  ; [ Expression ] ; [ Expression ] ) Statement
	private boolean forStatementRest() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.SEMICOLON)) {
			expression();
			if (expect(Separator.SEMICOLON)) {
				expression();
				if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
					if (statement())
					   accepted = true;
				}
			}
		}
		
		return accepted;
	}

//	ForInStatementRest ::= 
//	  in Expression ) Statement
	private boolean forInStatementRest() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.IN)) {
			if (expression()) {
				if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
					if (statement()) {
						accepted = true;
					}
				}
			}
		}		
		
		return accepted;
	}
//	ForStatementIdentifierPart ::=
//	  ForStatementAllRest
//	  MemberExpressionPart ForStatementAllRest
//	  ExpressionRest ForStatementRest
	private boolean forStatementIdentifierPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (forStatementAllRest()) {
			accepted = true;
		} else if (memberExpressionPart()) {
			if (forStatementAllRest()) {
				accepted = true;
			}
		} else if (expressionRest()) {
			if (forStatementRest()) {
				accepted = true;
			}
		} else if (noIdentifierComputableExpressionPart()) {
			if (expressionRest()) {
				if (forStatementRest()) {
					accepted = true;
				}
			}
		}

		
		return accepted;
	}
//	ForStatementAllRest::=
//	  ForStatementRest
//	  ForInStatementRest
	private boolean forStatementAllRest() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (forStatementRest()
			||forInStatementRest()) {
			accepted = true;
		}
		
		return accepted;
	}
	
//	ContinueStatement ::=  
//		continue [Identifier] ;
	private boolean continueStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.CONTINUE)) {
			accept(TokenType.IDENTIFIER);
			if(!accept(Separator.SEMICOLON)){
				consume(Separator.SEMICOLON);
			}
			accepted = true;
		}
		
		return accepted;
	}
//	BreakStatement ::=   
//		break [Identifier] ;
	private boolean breakStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.BREAK)) {
			accept(TokenType.IDENTIFIER);
			if(!accept(Separator.SEMICOLON)){
				consume(Separator.SEMICOLON);
			}
			accepted = true;
		}
		
		return accepted;
	}
//	ReturnStatement ::= 
//		return [Expression] ;
	private boolean returnStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.RETURN)) {
			expression();
			if(!accept(Separator.SEMICOLON)){
				consume(Separator.SEMICOLON);
			}
			accepted = true;
		}
		
		return accepted;
	}

//	WithStatement ::=  
//		with ( Expression ) Statement
	private boolean withStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.WITH)) {
			//fire begin scope
			beginBlock();

			if (expect(Separator.OPEN_PARAMETER_BLOCK)) {
				if (expression()){
					if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
						if (statement()) {
							accepted = true;
						}
					}
				}
			}
			
			//fire end scope
			endBlock();
		}
		
		return accepted;
	}

//	SwitchStatement ::= 
//		switch ( Expression ) CaseBlock
	private boolean switchStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.SWITCH)) {
			//fire begin scope
			beginBlock();

			if (expect(Separator.OPEN_PARAMETER_BLOCK)) {
				if (expression()) {
					if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
						if (caseBlock()){
							accepted = true;
						}
					}
				}
			}
			
			//fire begin scope
			endBlock();
		}
		
		return accepted;
	}
//	CaseBlock ::=  
//		{ [CaseClauses]  [DefaultClause] }
	private boolean caseBlock() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.OPEN_BLOCK)){
			caseClauses();
			defaultClause();
			expect(Separator.CLOSE_BLOCK);
			accepted = true;
		}
		
		return accepted;
	}	
//	CaseClauses ::= 
//		CaseClause CaseClauses
	private boolean caseClauses() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (caseClause()) {
			caseClauses();
			accepted = true;
		}
		
		return accepted;
	}
//	CaseClause ::=  
//		case Expression : [StatementList]
	private boolean caseClause() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.CASE)){
			if (expression()){
				if (expect(Operator.CONDITION_COLON)) {
					statementList();
					accepted = true;
				}
			}
		}
		
		return accepted;
	}
//	DefaultClause ::=  
//		default : [StatementList]
	private boolean defaultClause() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.DEFAULT)) {
			if (accept(Operator.CONDITION_COLON)) {
				statementList();
				accepted = true;
			}
		}
		
		return accepted;
	}
//	ThrowStatement ::=  
//		throw Expression ;
	private boolean throwStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		if (accept(Keyword.THROW)) {
			if (expression()) {
				if(!accept(Separator.SEMICOLON)){
					consume(Separator.SEMICOLON);
				}

				accepted = true;
			}
		}
		return accepted;
	}
//	TryStatement ::  
//		try Block [Catch] [Finally]
	private boolean tryStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.TRY)) {
			if (block()) {
				catchBlock();
				finallyBlock();
				accepted = true;
			}
		}
		
		return accepted;
	}
//	Catch ::=  
//		catch ( Identifier ) Block
	private boolean catchBlock() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.CATCH)) {
			//fire begin scope
			beginBlock();

			if (expect(Separator.OPEN_PARAMETER_BLOCK)) {
				if (expect(TokenType.IDENTIFIER)) {
					if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
						if (block()) {
							accepted = true;
						}
					}
				}
			}
			
			//fire begin scope
			endBlock();
		}
		
		return accepted;
	}
//	Finally ::=  
//		finally Block
	private boolean finallyBlock() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.FINALLY)) {
			if (block()) {
				accepted = true;
			}
		}		
		
		return accepted;
	}
//	IdentifierStatementPart ::=  
//		:  LabellableStatement               //labelled statement
//		IdentifierExpressionPart [ExpressionRest];

	private boolean identifierStatementPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Operator.CONDITION_COLON)) {
			labellableStatement();
			accepted = true;
		} else {
			identifierExpressionPart();
			expressionRest();
			accepted = true;
		}
		
		return accepted;
	}
	
//	ExpressionStatement ::=
//	       LiteralExpression [NoAssignmentExpressionRest]  ;
//	       ExpressionPart [ExpressionRest] ;
	private boolean expressionStatement() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (literalExpression()) {
			noAssignmentExpressionRest();
			if(!accept(Separator.SEMICOLON)){
				consume(Separator.SEMICOLON);
			}
			accepted = true;
		} else if (expressionPart()) {
			expressionRest();
			accepted = true;
			if(!accept(Separator.SEMICOLON)){
				consume(Separator.SEMICOLON);
			}
		}

		
		return accepted;
	}
//	NoIdentifierComputableExpressionPart ::=
//	        NewExpression
//	        FunctionExpression
//	        ArrayLiteralExpression
//	        ObjectLiteralExpression
//	        PrefixExpression
//			LiteralExpression
//		    ThisExpression
//    		( Expression )[. IdentifierExpression]
	
	private boolean noIdentifierComputableExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (newExpression()
			||functionExpression()
			||arrayLiteralExpression()
			||objectLiteralExpression()
			||prefixExpression()
			||literalExpression()) {
			accepted = true;
		} else if (thisExpression()){
			accepted = true;
		} else if (accept(Separator.OPEN_PARAMETER_BLOCK)) {
			if (expression()) {
				if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
					accepted = true;
					if (accept(Separator.PERIOD)) {
						// Expect Identifier Expression.
						if (identifierExpression()) {
							accepted =true;
						}						
					}
				}
			}
		}
		
		return accepted;
	}	
	
//	Expression ::=
//	       LiteralExpression [NoAssignmentExpressionRest]
//	       ExpressionPart [ExpressionRest]
	private boolean expression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (literalExpression()) {
			noAssignmentExpressionRest();
			accepted = true;
		} else if (expressionPart()) {
			expressionRest();
			accepted = true;
		}
		
		return accepted;
	}
//	ExpressionPart ::=
//	        ComputableExpressionPart
//	        DeleteExpression
//	        void PrefixExpression
	private boolean expressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (computableExpressionPart()
			||deleteExpression()) {
			accepted = true;
		} else if (accept(Keyword.VOID)) {
			if (prefixExpressionPart()) {
				accepted = true;
			} //TODO report error;
		}
		
		return accepted;
	}
//	ComputableExpressionPart ::=
//        IdentifierExpression   
//        NoIdentifierComputableExpressionPart	
	private boolean computableExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (identifierExpression()
			||noIdentifierComputableExpressionPart()) {
			accepted = true;
		}
		
		return accepted;
	}
//	ExpressionRest ::= 
//	        AssignmentExpressionPart
//	        NoAssignmentExpressionRest
	private boolean expressionRest() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (assignmentExpressionPart()) {
			accepted = true;
		} else if (noAssignmentExpressionRest()) {
			accepted = true;
		}

		
		return accepted;
	}
//	NoAssignmentExpressionRest ::= 
//	        , Expression
//	        ? Expression : Expression
//	        BinaryExpressionPart
//	        RelationalExpressionPart
	private boolean noAssignmentExpressionRest() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.COMMA)) {
			if (expression()) {
				accepted = true;
			}
		} else if (accept(Operator.CONDITION_QUESTION)) {
			if (expression()) {
				if (expect(Operator.CONDITION_COLON)) {
					if (expression()) {
						accepted = true;
					}
				}
			}
		} else if (binaryExpressionPart()
				   ||relationExpressionPart()) {
			accepted = true;
		}
		
		return accepted;
	}
//	NewExpression ::= 
//		new MemberExpression
	private boolean newExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.NEW)) {
			if (memberExpression()) {
				accepted = true;
			}
		}
		
		return accepted;
	}	
//	MemberExpression ::=  
//		Identifier [ MemberExpressionPart ]
	private boolean memberExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.IDENTIFIER)) {
			memberExpressionPart();
			accepted = true;
		}
		
		return accepted;
	}
//	MemberExpressionPart ::= . MemberExpression
//	                       [ [Expression] ] [MemberExpressionPart]
//	                       Arguments [MemberExpressionPart]
	private boolean memberExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.PERIOD)) {
			if (memberExpression()) {
				accepted = true;
			}
		} else if (accept(Separator.OPEN_ARRAY_BLOCK)) {
			expression();
			if (expect(Separator.CLOSE_ARRAY_BLOCK)) {
				memberExpressionPart();
				accepted =true;
			}
		} else if (arguments()) {
			memberExpressionPart();
			accepted =true;
		}
		
		return accepted;
	}
//	Arguments ::=  
//		( [ ArgumentList ] )
	private boolean arguments() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.OPEN_PARAMETER_BLOCK)) {
			argumentList();
			if (expect(Separator.CLOSE_PARAMETER_BLOCK)) {
				accepted = true;
			}
		}
		
		return accepted;
	}	

//	ArgumentList ::= 
//		Expression [, ArgumentList]
	private boolean argumentList() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (expression()) {
			if (accept(Separator.COMMA)) {
				if (argumentList()) {
					accepted = true;
				}
			}
		}
		
		return accepted;
	}	
	
//	IdentifierExpression ::= 
//	      Identifier [IdentifierExpressionPart]
	private boolean identifierExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.IDENTIFIER)) {
			identifierExpressionPart();
			accepted = true;
		}
		
		return accepted;
	}
//	IdentifierExpressionPart ::=  PostfixOperator
//	                              MemberExpressionPart PostfixOperator
	private boolean identifierExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.POSTFIX_OPERATOR)){
			accepted = true;
		} else if (memberExpressionPart()) {
			accept(TokenType.POSTFIX_OPERATOR);
			accepted = true;
		}
	
		return accepted;
	}	
//	AssignmentExpressionPart ::=  
//		AssignmentOperator Expression       
	private boolean assignmentExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.ASSIGNMENT_OPERATOR)) {
			// Register the symbol. 
			// The data type will known only when the
			// the right is parsed completely.
			if (expression()) {
				accepted = true;
			}
		}
		
		return accepted;
	}
//	FunctionExpression ::=  
//	        function [Identifier] FunctionDeclarationPart
	private boolean functionExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.FUNCTION)) {
			accept(TokenType.IDENTIFIER);
			if (functionDeclarationPart()) {
				accepted = true;
			}
		}
		
		return accepted;
	}
//	LiteralExpression ::=  
//	        NumericLiteral
//	        BooleanLiteral
//	        StringLiteral [ MemberExpressionPart ]
//	        NullLiteral
//			RegexLiteral	
	private boolean literalExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.NUMERIC_LITERAL)
			||accept(TokenType.BOOLEAN_LITERAL)
			||accept(TokenType.NULL_LITERAL)
			||accept(TokenType.REGEX_LITERAL)) {
			accepted = true;
		} else if (accept(TokenType.STRING_LITERAL)) {
			// TODO do member expression part.
			accepted = true;
		}
		
		return accepted;
	}

//	ThisExpression ::=
//			this [ThisExpressionPart]
	private boolean thisExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		if (accept(Keyword.THIS)) {
			if (thisExpressionPart()) {
				accepted = true;
			}
		}		
		return accepted;
	}
//	ThisExpressionPart ::=
//		  	arrayLiteralExpression
//			. IdentifierExpression
	private boolean thisExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		if (arrayLiteralExpression()) {
			accepted = true;
		} else if (accept(Separator.PERIOD)){
			if (identifierExpression()) {
				accepted = true;
			}
		}
		return accepted;
	}
	
//	ArrayLiteralExpression :   
//	        [  ArrayLiteralExpressionPart  ]
	private boolean arrayLiteralExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.OPEN_ARRAY_BLOCK)) {
			arrayLiteralExpressionPart();
			if (expect(Separator.CLOSE_ARRAY_BLOCK)) {
				accepted = true;
			}
		}
		
		return accepted;
	}
//	ArrayLiteralExpressionPart ::=
//	        , [ArrayLiteralExpressionPart]
//	        SingularExpression  [ArrayLiteralExpressionPart]
	private boolean arrayLiteralExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.COMMA)) {
			arrayLiteralExpressionPart();
			accepted = true;
		} else if (singularExpression()) {
			arrayLiteralExpressionPart();
			accepted = true;
		}
		
		return accepted;
	}
//TODO: resolve ambiguity between simple block and objectliteral expression
//	ObjectLiteralExpression ::= 
//		{ [PropertyNameAndValueList] }
	private boolean objectLiteralExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Separator.OPEN_BLOCK)){
			propertyNameAndValueList();
			if (expect(Separator.CLOSE_BLOCK)) {
				accepted = true;
			}
		}
		return accepted;
	}
//	PropertyNameAndValueList ::= 
//		PropertyName : SingularExpression  [ , PropertyNameAndValueList ]
	private boolean propertyNameAndValueList() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (propertyName()) {
			// TODO rename this operator name
			if (expect(Operator.CONDITION_COLON)){
				if (singularExpression()) {
					if (accept(Separator.COMMA)) {
						propertyNameAndValueList();
					}
					accepted = true;
				}
			}
		}
		
		return accepted;
	}
//	PropertyName ::=   Identifier
//	                   StringLiteral
//	                   NumericLiteral
	private boolean propertyName() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.IDENTIFIER)
			||accept(TokenType.STRING_LITERAL)
			||accept(TokenType.NUMERIC_LITERAL)) {
			accepted = true;
		}
		
		return accepted;
	}
//	DeleteExpression ::=
//	        delete DeleteExpressionPart
	private boolean deleteExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.DELETE)) {
			if (deleteExpressionPart()) {
				accepted = true;
			}
		}
		
		return accepted;
	}
//	DeleteExpressionPart ::=
//	       NewExpression
//	       FunctionExpression
//	       ArrayLiteralExpression
//	       ObjectLiteralExpression
//	       ThisExpression
//	       (  RightHandExpression )
//	       IdentifierExpression
	private boolean deleteExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (newExpression()
			||functionExpression()
			||arrayLiteralExpression()
			||objectLiteralExpression()
			||identifierExpression()) {
			accepted = true;
		} else if (thisExpression()) {
					accepted = true;
		}
		
		return accepted;
	}
//	PrefixExpression ::=
//	      typeof PrefixExpressionPart
//	      ++ PrefixExpressionPart
//	       -- PrefixExpressionPart
//	      + PrefixExpressionPart
//	      - PrefixExpressionPart
//	      ~ PrefixExpressionPart
//	      ! PrefixExpressionPart
	private boolean prefixExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Keyword.TYPEOF)
			|| accept(Operator.INCREMENT)
			|| accept(Operator.DECREMENT)
			|| accept(Operator.ADDITION)
			|| accept(Operator.SUBTRACTION)
			|| accept(Operator.BITWISE_COMPLEMENT)
			|| accept(Operator.NOT)) {
			if (prefixExpressionPart()) {
				accepted = true;
			}
		}
		
		return accepted;
	}
//	PrefixExpressionPart ::=
//	        NewExpression            
//	        this . IdentifierExpression
//	        (  PrefixExpression )
//	        IdentifierExpression
	private boolean prefixExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (newExpression()
			||identifierExpression()) {
			accepted = true;
		} else if (accept(Keyword.THIS)) {
			if (expect(Separator.PERIOD)) {
				if (identifierExpression()) {
					accepted = true;
				}
			}
		}
		
		return accepted;
	}
//	BinaryExpressionPart ::=
//	        +   SingularExpression
//	        -   SingularExpression
//	        /   SingularExpression
//	        %   SingularExpression
//	        <<  SingularExpression
//	        >>  SingularExpression
//	        >>> SingularExpression
//	        &   SingularExpression
//	        ^   SingularExpression
//	        |   SingularExpression
	private boolean binaryExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.ARITHEMATIC_OPERATOR)
			||accept(TokenType.SHIFT_OPERATOR)
			||accept(Operator.BITWISE_OR)
			||accept(Operator.BITWISE_XOR)
			||accept(Operator.BITWISE_AND)) {
			if (singularExpression()) {
				accepted = true;
			}
		}
			
		return accepted;
	}                                  
//	RelationalExpressionPart ::=
//	        < SingularExpression
//	        > SingularExpression
//	        <= SingularExpression
//	        >= SingularExpression
//	        == SingularExpression
//	        != SingularExpression
//	        || SingularExpression
//	        &&  SingularExpression
//	        instanceof  PrefixExpressionPart
	private boolean relationExpressionPart() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(TokenType.RELATIONAL_OPERATOR)) {
			if (singularExpression()) {
				accepted = true;
			}
		} else if (accept(Keyword.INSTANCEOF)) {
			if (prefixExpressionPart()) {
				accepted = true;
			}
		}
		
		return accepted;
	}
	
//SingularExpression ::=
//        LiteralExpression [SingularNoAssignmentExpressionRest]
//        ExpressionPart [SingularExpressionRest]
	private boolean singularExpression() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (literalExpression()) {
			singularNoAssignmentExpressionRest();
			accepted = true;
		} else if (expressionPart()) {
			singularNoAssignmentExpressionRest();
			accepted = true;
		}
		
		return accepted;
	}
//SingularExpressionRest ::=
//        AssignmentExpressionPart
//        SingularNoAssignmentExpressionRest
	private boolean singularExpressionRest() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (assignmentExpressionPart()) {
			accepted = true;
		} else if (singularNoAssignmentExpressionRest()) {
			accepted = true;
		}
		
		return accepted;
	}

//SingularNoAssignmentExpressionRest ::=
//        ? SingularExpression : SingularExpression
//        BinaryExpressionPart
//        RelationalExpressionPart
	private boolean singularNoAssignmentExpressionRest() {
		boolean accepted = false; if (this.lookAhead==null) return accepted;
		
		if (accept(Operator.CONDITION_QUESTION)) {
			if (singularExpression()) {
				if (expect(Operator.CONDITION_COLON)) {
					if (singularExpression()) {
						accepted = true;
					}
				}
			}
		} else if (binaryExpressionPart()
				   ||relationExpressionPart()) {
			accepted = true;
		}
		
		return accepted;
	}
}