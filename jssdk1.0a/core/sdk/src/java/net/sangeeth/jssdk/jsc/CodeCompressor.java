package net.sangeeth.jssdk.jsc;

import java.io.PrintWriter;
import java.io.Writer;

public class CodeCompressor extends JSParserAdapter {
	private PrintWriter writer;
	public CodeCompressor(Writer writer) {
		this.writer = new PrintWriter(writer);
	}
	public void beginBlock() {
		writer.println();
	}

	public void consumedToken(Token token) {
		TokenType tokenType = token.getType();
		writer.print(token);
		if (tokenType==TokenType.KEYWORD) {
			writer.print(' ');
		}
		writer.flush();
	}

	public void endBlock() {
		writer.println();
	}
}
