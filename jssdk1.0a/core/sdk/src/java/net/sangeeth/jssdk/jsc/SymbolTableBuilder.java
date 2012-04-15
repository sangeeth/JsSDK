package net.sangeeth.jssdk.jsc;

import java.util.Stack;

public class SymbolTableBuilder extends JSParserAdapter {
	private Stack<Scope> scopeStack;
	private Scope activeScope;
	private Token identifier;
//	Scope newScope = new Scope();
//	activeScope.addScope(newScope);
//	scopeStack.push(activeScope);
//	activeScope = newScope;
	
	private int depth = 0;
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

	public void declaredDirective(DirectiveRef directive) {
		// TODO Auto-generated method stub
		
	}

	public void definedSymbol(Symbol symbol) {
		// TODO Auto-generated method stub
		
	}

	public void endBlock() {
		depth--;
		for(int i=0;i<depth;i++) {
			System.out.print('\t');
		}
		System.out.println("}");
//		activeScope = scopeStack.pop();
	}

	public void finished(boolean success) {
		// TODO Auto-generated method stub
		
	}

	public void started() {
		// TODO Auto-generated method stub
		
	}

}

/*public void addSymbol(SymbolType symbolType) {
	if (identifier!=null) {
		this.activeScope.addSymbol(new Symbol(identifier.getValue(),symbolType));
		this.identifier = null;
	} else if (lastUnknownSymbol!=null) {
		lastUnknownSymbol.setSymbolType(symbolType);
		this.activeScope.addSymbol(lastUnknownSymbol);
		this.lastUnknownSymbol = null;
	}
}

	public void addSymbol() {
		if (identifier!=null) {
			lastUnknownSymbol = new Symbol(identifier.getValue(),null);
			identifier = null;
		}
	}

	public void endSymbolDefinition() {
		if (this.lastUnknownSymbol!=null) {
			addSymbol(SymbolType.VARIABLE);
		} 
		this.lastUnknownSymbol = null;
	}
		activeScope = new Scope("PROGRAM");

*/
