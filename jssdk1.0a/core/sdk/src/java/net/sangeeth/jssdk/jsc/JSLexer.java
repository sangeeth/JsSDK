package net.sangeeth.jssdk.jsc;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static net.sangeeth.jssdk.jsc.TokenType.*;
public class JSLexer {
	private StreamTokenizer st; 
	private Token currentToken;
	private int lineno;

	boolean trace = false;
	private void consumeRoute() {
		if (!stack.isEmpty()) {
			trace = false;
			tokenList = stack.pop();
			if (tokenList!=null) {
				tokenList.clear();
				tokenList=null;
			}
		}
	}
	private void traceRoute() {
		trace = true;
		tokenList = new ArrayList<Object>();
		stack.push(tokenList);
	}
	private void rollback() {
		if (!stack.isEmpty()) {
			trace = false;
			tokenList = stack.pop();
		}
	}
	
	private List<Object> tokenList = null;
	private StringBuffer regExLiteral = new StringBuffer();
	private Object lookahead = null;
	private static final Character EOL = new Character('\n');
	private static final Character WHITESPACE = new Character(' ');
	private Stack<List<Object>> stack = new Stack<List<Object>>();
	public JSLexer(Reader in) {
		st = new StreamTokenizer(in);
		
        // Prepare the tokenizer for Java-style tokenizing rules		
        st.parseNumbers();
        st.wordChars('_', '_');
        st.wordChars('$', '$');
        st.eolIsSignificant(true);
    
        st.ordinaryChar('.');
        st.ordinaryChar('\\');
        st.ordinaryChar('/');
        st.ordinaryChar('+');
        st.ordinaryChar('-');
        // If whitespace is not to be discarded, make this call
        st.ordinaryChars(0, WHITESPACE);
    
        // These calls caused comments to be discarded
        st.slashSlashComments(true);
        st.slashStarComments(true);
        
        lineno = 1;
	}
	public Token currentToken() {
		return this.currentToken;
	}
	private String escape(String literal) {
		if (literal.length()>0) {
			literal = literal.replace("\"","\\\"");
			literal = literal.replace("\n","\\n");
			literal = literal.replace("\t","\\t");
			literal = literal.replace("\r","\\r");
		}
		return literal;
	}
	private Object getNextToken() {
		this.lookahead = null;
		if (!trace && tokenList!=null && !tokenList.isEmpty()) {
			this.lookahead = tokenList.remove(0);
			if (tokenList.isEmpty()) {
				this.tokenList = null;
			}
		} else {
			try {
		        int tokenType = st.nextToken();
		        if (tokenType != StreamTokenizer.TT_EOF) {
		            switch (tokenType) {
		            case StreamTokenizer.TT_NUMBER:
		                // A number was found; the value is in nval
		            	this.lookahead = st.nval;
		                break;
		            case StreamTokenizer.TT_WORD:
		            	this.lookahead = st.sval;
		                break;
		            case '\r':
		            	this.lookahead = EOL;
		            	break;
		            case '\t':
		            case ' ':
		            	this.lookahead = WHITESPACE;
		            	break;
					case '"':
						this.lookahead = "\""+ escape(st.sval) + "\"";
						break;
		            case '\'':
		                // A single-quoxdted string was found; sval contains the contents
		            	this.lookahead ="\'" + escape(st.sval) +"\'";
		                break;
		            case StreamTokenizer.TT_EOL:
		            	lineno++;
		            	this.lookahead = EOL;
		                break;
		            default:
		            	this.lookahead = Character.valueOf((char)st.ttype);
		                break;
		            }
		        }
		        if (trace) {
		        	this.tokenList.add(this.lookahead);
		        }
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		return this.lookahead;
	}
	public Token nextToken() {
		Token token = null;
		
		this.lookahead = this.getNextToken();

		if (lookahead!=null) {
			if (lookahead instanceof Number) {
				try {
					token = numericLiteral((Number)lookahead);
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			} else if (lookahead instanceof String) {
				String text = (String)lookahead;
				if (text.startsWith("\"")) {
					token = new Token(text,STRING_LITERAL);
				} else if (text.startsWith("'")) {
					token = new Token(text,STRING_LITERAL);
				} else {
	            	Keyword keyword = Keyword.valueOf((String)lookahead);
	            	if (keyword!=null) {
	            		token = keyword;
	            	} else {
	            		Directive directive = Directive.valueOf((String)lookahead);
	            		if (directive!=null) {
	            			token = directive;
	            		} else token = new Token((String)lookahead,IDENTIFIER);
	            	}
				}
			} else if (lookahead instanceof Character) {
				if (lookahead == EOL 
					||lookahead == WHITESPACE) {
					token = nextToken();
				}else {
					char ch = (Character)lookahead;
					try {
						token = operator(ch);
		            	if (token==null) {
		            		token = Separator.valueOf(String.valueOf(ch));
		            	} else if (token==Operator.DIVISION){
		            		Token t = regularExpressionLiteral();
		            		if (t != null) {
		            			token = t;
		            		}
		            	}
					} catch(IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
        this.currentToken = token;
        return token;
	}

	private Token numericLiteral(Number value) throws IOException {
		Token numericLiteral = null;
		if (value.doubleValue() == 0) {
			Object next = this.getNextToken();
			if (next instanceof String
				&& ((String)next).toLowerCase().startsWith("x")) {
				numericLiteral = new Token("0"+next,NUMERIC_LITERAL);
			} else {
				st.pushBack();
			}
		}
		if (numericLiteral==null) {
			numericLiteral = new Token(String.valueOf(value),NUMERIC_LITERAL);			
		}
		return numericLiteral;
	}
	
	private Operator operator(char ch) throws IOException{
		Object t = null;
		Operator token = null;
		char nextCh;
		String op;
		switch(ch ){
//		~    ?    :
		case '~': case '?':  case ':':
			token = Operator.valueOf(String.valueOf(ch));
			break;
//		=    /    !     ^     %    *
//		==   /=   !=    ^=    %=   *= 
//		===       !==
		case '=':  case '/':  case '!':  case '^':  case '%':  case '*':			
			t = getNextToken();
			if (t instanceof Character
				&& (nextCh = (Character)t)=='='){ 
				op = ch+"=";
				
				t = getNextToken();
				if (t instanceof Character
					&& (nextCh = (Character)t)=='='){ 
					op+="=";
					token = Operator.valueOf(op);
				} else {
					st.pushBack();
					token = Operator.valueOf(op); 
				}
				//token = Operator.valueOf(op);
			} else {
				st.pushBack();
				token = Operator.valueOf(String.valueOf(ch)); 
			}
			break;	
//			>      >=  
//			>>     >>=  
//			>>>    >>>=			
		case '>':
			t = getNextToken();
			if (t instanceof Character 
				&& (nextCh = (Character)t)=='='){ 
					token = Operator.GREATER_THAN_OR_EQUAL;
			} else if (t instanceof Character 
					&& (nextCh = (Character)t)=='>'){
				t = getNextToken();
				if (t instanceof Character
					&& (nextCh = (Character)t)=='>') {
					t = getNextToken();
					if (t instanceof Character 
						&& (nextCh = (Character)t)=='=') {
						token = Operator.SHIFT_DOUBLE_RIGHT_EQUAL;
					}else {
						st.pushBack();
						token = Operator.SHIFT_DOUBLE_RIGHT;
					}
				} else if (t instanceof Character
						&& (nextCh = (Character)t)=='=') {
					token = Operator.SHIFT_RIGHT_EQUAL; 
				} else {
					st.pushBack();
					token = Operator.SHIFT_RIGHT; 
				}					
			} else {
				st.pushBack();
				token = Operator.GREATER_THAN;
			}	
			break;
//			<    <=     
//			<<   <<=    
		case '<':
			t = this.getNextToken();
			if (t instanceof Character
				&& (nextCh = (Character)t)=='=') {
				token = Operator.LESSER_THAN_OR_EQUAL;
			} else if (t instanceof Character
					&& (nextCh = (Character)t)=='<') {
				t = this.getNextToken();
				if (t instanceof Character
						&& (nextCh = (Character)t)=='=') {
					token = Operator.SHIFT_LEFT_EQUAL; //new Token("<<=",Token.OPERATOR);
				} else {
					st.pushBack();
					token = Operator.SHIFT_LEFT; //new Token("<<",Token.OPERATOR);
				}					
			} else {
				st.pushBack();
				token = Operator.LESSER_THAN;
			}
			break;
//			+    -    &    |
//			++   --   &&   ||   
//			+=   -=   &=   |=   			
		case '+':  case '-':  case '&': case '|':			
			t = this.getNextToken();
			if (t instanceof Character
				&& (nextCh = (Character)t)=='=') {
				token = Operator.valueOf(ch+"=");
			} else {
				if (t instanceof Character
					&& (nextCh = (Character)t)== ch) {
					token = Operator.valueOf(ch+""+nextCh);
				} else {
					st.pushBack();
					token =Operator.valueOf(String.valueOf(ch));
				}
			}
			break;
		}
		return token;
	}
	public int getLineNumber() {
		return this.lineno;
	}
	
//	RegularExpressionLiteral	::=
//		RegularExpressionBody / RegularExpressionFlags
	private Token regularExpressionLiteral() throws IOException {
		Token token = null;
		boolean accepted = false;
		
		regExLiteral.setLength(0);
		regExLiteral.append("/");
		
		traceRoute();
		
		getNextToken();
		
		if (regularExpressionBody()) {
			char ch = (char)st.ttype;
			if (ch == '/'){
				regExLiteral.append(this.lookahead);
				accepted = true;
				getNextToken();
				if (regularExpressionFlags()) {
					//getNextToken();
					accepted = true;
				} else {
					st.pushBack();
				}
			}
		}
		if (!accepted) 
			rollback();
		else {
			consumeRoute();
			token = new Token(regExLiteral.toString(),TokenType.REGEX_LITERAL);
		}
		
		return token;
	}
//	RegularExpressionBody ::=
//		RegularExpressionFirstChar [RegularExpressionChars]
	private boolean regularExpressionBody() throws IOException{
		boolean accepted = false;
		if (regularExpressionFirstChar()) {
			getNextToken();
			accepted = true;
			regularExpressionChars();
		}
		return accepted;
	}
//	RegularExpressionChars ::=
//		RegularExpressionChar RegularExpressionChars 
	private boolean regularExpressionChars() throws IOException {
		boolean accepted = false;;
		
		while(regularExpressionChar()){
			getNextToken();
			accepted = true; 
		}
		
		return accepted;
	}

//	RegularExpressionFirstChar ::=
//		NonTerminator but not * or \ or /
//		BackslashSequence
	private boolean regularExpressionFirstChar() throws IOException  {
		boolean accepted = false;
		char ch;
		if (this.lookahead instanceof Character
			&& ((ch = (Character)this.lookahead)=='*'
				|| ch=='/')) {
				accepted = false;
		}  else if (nonTerminator()
				  ||backslashSequence()) {
			accepted = true;
		}		
		return accepted;
	}
		
//	RegularExpressionChar ::=
//		NonTerminator but not \ or /
//		BackslashSequence
	private boolean regularExpressionChar()  throws IOException {
		boolean accepted = false;
		
		if (this.lookahead instanceof Character
			&& ((Character)this.lookahead) == '/') {
			accepted = false;
		} else if (backslashSequence()) {
			accepted = true;
		} else if (nonTerminator()) {
			accepted = true;
		}
		
		return accepted;		
	}
	
//	BackslashSequence ::=
//		\ NonTerminator
	private boolean backslashSequence() throws IOException {
		boolean accepted = false;
		
		if (this.lookahead instanceof Character
			&& ((Character)this.lookahead) == '\\') {
			regExLiteral.append(this.lookahead);
			getNextToken();
			if (nonTerminator()) {
				accepted = true;
			} else {
				st.pushBack();
			}
		}
		
		return accepted;
	}
		
//	NonTerminator ::=
//		SourceCharacter but not LineTerminator
	private boolean nonTerminator() throws IOException {
		boolean accepted = false;
		
		if (st.ttype == StreamTokenizer.TT_EOF
			|| st.ttype == StreamTokenizer.TT_EOL) {
			accepted = false;
		} else if (lookahead instanceof Character
				||lookahead instanceof String
				||lookahead instanceof Number) {
			regExLiteral.append(this.lookahead);
			accepted = true;
		}
		
		return accepted;
	}
		
//	RegularExpressionFlags ::=
//		[empty]
//		RegularExpressionFlags IdentifierPart	
	private boolean regularExpressionFlags() {
		boolean accepted = false;
		if (lookahead instanceof String
			||lookahead instanceof Number) {
			regExLiteral.append(this.lookahead);
			accepted = true;
		}
		return accepted;
	}
	
}
