package net.sangeeth.jssdk.jsc;

public interface JSParserListener {
	public void started();
	public void finished(boolean success);
	public void consumedToken(Token token);
	public void beginBlock();
	public void endBlock();
	public void declaredDirective(DirectiveRef directive);
	public void beginScope();
	public void endScope();
	public void definedSymbol(Symbol symbol);
}
                 