package core;
import static net.sangeeth.jssdk.jsc.TokenType.IDENTIFIER;
import static net.sangeeth.jssdk.jsc.TokenType.NUMERIC_LITERAL;
import static net.sangeeth.jssdk.jsc.TokenType.STRING_LITERAL;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

import net.sangeeth.jssdk.jsc.JSLexer;
import net.sangeeth.jssdk.jsc.Keyword;
import net.sangeeth.jssdk.jsc.Separator;
import net.sangeeth.jssdk.jsc.Token;


public class TestLexer {
	public static void main(String [] arg) throws Exception {
		File file = new File("test.js");
		Reader reader = new FileReader(file);
		
		JSLexer lexer = new JSLexer(reader);
		Token token;
		while((token = lexer.nextToken())!=null ){
			System.out.println(token.getValue());
		}
	}
}