package net.sangeeth.jssdk.jsp.el;

import java.io.IOException;
import java.io.StreamTokenizer;
import static net.sangeeth.jssdk.jsp.el.TokenType.*;
import java.io.StringReader;

public class ELTokenizer {
	private StreamTokenizer st; 
	private Token currentToken;

	public ELTokenizer(String expression) {
		st = new StreamTokenizer(new StringReader(expression));
        
        // Prepare the tokenizer for Java-style tokenizing rules		
        st.parseNumbers();
        st.wordChars('_', '_');
        st.eolIsSignificant(true);
        
        st.ordinaryChars('.','.');
        
        // If whitespace is not to be discarded, make this call
        //st.ordinaryChars(0, ' ');
    
        // These calls caused comments to be discarded
        st.slashSlashComments(true);
        st.slashStarComments(true);
	}
	public Token currentToken() {
		return this.currentToken;
	}
	public Token nextToken() {
		Token token = null;
		try {
	        // Parse the file
	        int tokenType = st.nextToken();
	        if (tokenType != StreamTokenizer.TT_EOF) {
	            switch (tokenType) {
	            case StreamTokenizer.TT_NUMBER:
	                // A number was found; the value is in nval
	                token = new Token(String.valueOf(st.nval),NUMERIC_LITERAL);
	                break;
	            case StreamTokenizer.TT_WORD:
	                // A word was found; the value is in sval
	            	
	            	Keyword keyword = Keyword.valueOf(st.sval);
	            	if (keyword!=null) {
	            		token = keyword;
	            	} else {
	            		ImplicitObject implicitObject = ImplicitObject.valueOf(st.sval);
	            		if (implicitObject!=null) {
	            			token = implicitObject;
	            		} else {
	            			Operator op = Operator.valueOf(st.sval);
	            			if (op!=null) {
	            				token = op;
	            			}
	            			else token = new Token(st.sval,IDENTIFIER);
	            		}
	            	}
	                break;
	            case '"':
	                // A double-quoted string was found; sval contains the contents
	                token = new Token("\""+ st.sval + "\"",STRING_LITERAL);
	                break;
	            case '\'':
	                // A single-quoxdted string was found; sval contains the contents
	                token = new Token("\'" + st.sval +"\'",STRING_LITERAL);
	                break;
	            case StreamTokenizer.TT_EOL:
	            	token = nextToken();
	                // End of line character found
	                break;
	            default:
	                // A regular character was found; the value is the token itself
	                char ch = (char)st.ttype;
	            	token = operator(ch);
	            	if (token==null) {
	            		token = Separator.valueOf(String.valueOf(ch));
	            	}
	                break;
	            }
	        }
	        this.currentToken = token;
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
        return token;
	}
	private Operator operator(char ch) throws IOException{
		Operator token = null;
		char nextCh;
		String op;
		switch(ch ){
		case '=': 	
			st.nextToken();
			nextCh = (char)st.ttype;
			if (nextCh=='=') {
				op = ch+"=";
				token = Operator.valueOf(op);
			} else {
				st.pushBack();
			}
			break;	
//		~    ?    :
		case '~': case '?':  case ':':
			token = Operator.valueOf(String.valueOf(ch));
			break;
//		/    !     ^     %    *
		case '/':  case '!':  case '^':  case '%':  case '*':			
			token = Operator.valueOf(String.valueOf(ch)); //new Token(String.valueOf(ch),Token.OPERATOR);
			break;	
//			>      >=  
		case '>':
			st.nextToken();
			nextCh = (char)st.ttype;
			if (nextCh=='=') {
				token = Operator.GREATER_THAN_OR_EQUAL;//new Token(">=",Token.OPERATOR);
			} else {
				st.pushBack();
				token = Operator.GREATER_THAN;
			}
			break;
//			<    <=     
		case '<':
			st.nextToken();
			nextCh = (char)st.ttype;
			if (nextCh=='=') {
				token = Operator.LESSER_THAN_OR_EQUAL;
			} else {
				st.pushBack();
				token = Operator.LESSER_THAN;
			}
			break;
//			+    -    &    |
//			++   --   &&   ||   
//			+=   -=   &=   |=   			
		case '+':  case '-':  case '&': case '|':			
			st.nextToken();
			char nextch = (char)st.ttype;
			if (nextch=='=') {
				token = Operator.valueOf(ch+"=");
			} else {
				st.nextToken();
				nextCh = (char)st.ttype;
				if (nextCh == ch) {
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
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ELTokenizer tokenizer = new ELTokenizer("(post.title == '10')?true:((empty param.name)?true:false)");
		Token token;
		while((token=tokenizer.nextToken())!=null) {
			System.out.println(token);
		}
	}	
}
