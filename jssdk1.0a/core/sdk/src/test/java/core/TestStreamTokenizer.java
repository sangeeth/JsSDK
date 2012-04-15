package core;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;


public class TestStreamTokenizer {

	private static StreamTokenizer st;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		File file = new File("C:/sangeeth/works/eclipse/personal/Blogger/WebContent/scripts/js/specimen.js");
		//File file = new File("src/js/jsdk/jsdk.js");
		Reader reader = new FileReader(file);

		st = new StreamTokenizer(reader);	
		
        // Prepare the tokenizer for Java-style tokenizing rules		
        st.parseNumbers();
        st.wordChars('_', '_');
        st.eolIsSignificant(true);
    
        st.ordinaryChar('.');
        st.ordinaryChar('/');
        st.ordinaryChar('+');
        st.ordinaryChar('-');
        // If whitespace is not to be discarded, make this call
        //st.ordinaryChars(0, ' ');
    
        // These calls caused comments to be discarded
        st.slashSlashComments(true);
        st.slashStarComments(true);

        int tokenType = st.nextToken();
        while (tokenType != StreamTokenizer.TT_EOF) {
            switch (tokenType) {
	            case StreamTokenizer.TT_NUMBER:
	            	System.out.println(st.nval);
	                break;
	            case StreamTokenizer.TT_EOL:
	            case '\'':
	            	break;
	            case '"':
	            	break;
	            case StreamTokenizer.TT_WORD:
	            	System.out.println("word = " +st.sval);
	                break;
	            default:
	                char ch = (char)st.ttype;
	            	System.out.println(ch);
	            	if (ch == '/') {
	            		regularExpressionLiteral();
//	            		Object token = null;
//	            		while((token=nextToken())!=null) {
//	            			System.out.println(token);
//	            		}
	            	}
	                break;
	        }
            tokenType = st.nextToken();
        }
        
        reader.close();
	}
	
	
	
	
	private static StringBuffer regExLiteral = new StringBuffer();
	private static int lookAheadCount;
	private static Object lookahead = null;
	private static void resolveToken(int tokenType) {
        switch (tokenType) {
        case StreamTokenizer.TT_NUMBER:
        	lookahead = st.nval;
            break;
        case StreamTokenizer.TT_EOL:
        	lookahead = null;
        	break;
        case StreamTokenizer.TT_WORD:
        	lookahead = st.sval;
            break;
        default:
            char ch = (char)st.ttype;
        	lookahead = Character.valueOf(ch);
            break;
        }		
	}
	private static Object nextToken()  throws IOException {
		lookahead = null;
		int tokenType = st.nextToken();
		resolveToken(tokenType);
        if (lookahead!=null) {
			regExLiteral.append(lookahead);
			System.out.println("regex = " + regExLiteral.toString());
			++lookAheadCount;
        }
        return lookahead;
	}
	private static void rollback() {
		for(int i=0;i<lookAheadCount+1;i++) {
			st.pushBack();
			resolveToken(st.ttype);
		}
	}
	
//	RegularExpressionLiteral	::=
//		RegularExpressionBody / RegularExpressionFlags
	private static boolean regularExpressionLiteral() throws IOException {
		boolean accepted = false;
		lookAheadCount = 0;
		regExLiteral.setLength(0);
		regExLiteral.append("/");
		nextToken();
		if (regularExpressionBody()) {
			char ch = (char)st.ttype;
			if (ch == '/'){
					accepted = true;
//					lookahead();
//					if (regularExpressionFlags()) {
//						accepted = true;
//					}
			}
		}
		if (!accepted) rollback();
		else System.out.println("regexliteral  = " + regExLiteral.toString());
		
		return accepted;
	}
//	RegularExpressionBody ::=
//		RegularExpressionFirstChar [RegularExpressionChars]
	private static boolean regularExpressionBody() throws IOException{
		boolean accepted = false;
		if (regularExpressionFirstChar()) {
			nextToken();
			accepted = true;
			regularExpressionChars();
		}
		return accepted;
	}
//	RegularExpressionChars ::=
//		RegularExpressionChar RegularExpressionChars 
	private static boolean regularExpressionChars() throws IOException {
		boolean accepted = false;;
		
		while(regularExpressionChar()){
			accepted = true; 
		}
		
		return accepted;
	}

//	RegularExpressionFirstChar ::=
//		NonTerminator but not * or \ or /
//		BackslashSequence
	private static boolean regularExpressionFirstChar() throws IOException  {
		boolean accepted = false;
		
		char ch = (char)st.ttype;
		if (ch=='*'||ch == '/') {
			accepted = false;
		} else if (nonTerminator()
				  ||backslashSequence()) {
			accepted = true;
		}
		
		return accepted;
	}
		
//	RegularExpressionChar ::=
//		NonTerminator but not \ or /
//		BackslashSequence
	private static boolean regularExpressionChar()  throws IOException {
		boolean accepted = false;
		
		char ch = (char)st.ttype;
		if (ch == '/') {
			accepted = false;
		} else if (backslashSequence()) {
			nextToken();
			accepted = true;
		} else if (nonTerminator()) {
			nextToken();
			accepted = true;
		}
		
		return accepted;		
	}
	
//	BackslashSequence ::=
//		\ NonTerminator
	private static boolean backslashSequence() throws IOException {
		boolean accepted = false;
		
		char ch = (char)st.ttype;
		if (ch == '\\') {
			nextToken();
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
	private static boolean nonTerminator() throws IOException {
		boolean accepted = false;
		
		if (st.ttype == StreamTokenizer.TT_EOF
			|| st.ttype == StreamTokenizer.TT_EOL) {
			accepted = false;
		} else if (lookahead instanceof Character
				||lookahead instanceof String
				||lookahead instanceof Number) {
			accepted = true;
		}
		
		return accepted;
	}
		
//	RegularExpressionFlags ::=
//		[empty]
//		RegularExpressionFlags IdentifierPart	
	private static boolean regularExpressionFlags() {
		boolean accepted = false;
		
		return accepted;
	}
}
