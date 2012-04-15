package net.sangeeth.jssdk.jsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class Scope {

	private static Stack<Scope> scopeStack;	
	private static Scope instance = null;
	public static Scope getInstance() {
		if (instance == null) {
			instance = new Scope();
			scopeStack = new Stack<Scope>();
			scopeStack.push(instance);
		}
		return instance;
	}
	private Set<String> definedSymbols;
	private Set<String> symbols;
	private List<Scope> scopes;
	private Scope() {
		symbols = new TreeSet<String>();
		definedSymbols = new TreeSet<String>();
		scopes = new ArrayList<Scope>();
	}
	public void beginScope() {
		Scope currentScope = scopeStack.peek();
		Scope newScope = new Scope();
		currentScope.scopes.add(newScope);
		scopeStack.push(newScope);
	}
	public void defineSymbol(String symbol) {
		Scope scope = scopeStack.peek();
		scope.definedSymbols.add(symbol);
	}
	public void addSymbols(Set<String> symbols) {
		Scope scope = scopeStack.peek();
		scope.symbols.addAll(symbols);
	}	
	public void addSymbol(String symbol) {
		Scope scope = scopeStack.peek();
		scope.symbols.add(symbol);
	}
	public void endScope() {
		scopeStack.pop();
	}
	public List<Scope> getScopes() {
		return scopes;
	}
	public Set<String> getSymbols() {
		return symbols;
	}
	public Set<String> getDefinedSymbols() {
		return definedSymbols;
	}
	public Set<String> getUndefinedSymbols() {
		Set<String> undefinedSymbols = new TreeSet<String>();
		undefinedSymbols.addAll(symbols);
		undefinedSymbols.removeAll(definedSymbols);
		return undefinedSymbols;
	}
	public Set<String> getAllUndefinedSymbols() {
		Set<String> undefinedSymbols;
		if (!scopes.isEmpty()) {
			undefinedSymbols = new TreeSet<String>();
			undefinedSymbols.addAll(this.getUndefinedSymbols());
			for(Scope scope:scopes) {
				undefinedSymbols.addAll(scope.getAllUndefinedSymbols());
			}
		} else {
			undefinedSymbols = this.getUndefinedSymbols();
		}
		return undefinedSymbols;
	}
	public void list() {
		for(String symbol:symbols) {
			System.out.print(symbol+",");
		}
		System.out.println();
		for(Scope scope:scopes) {
			scope.list();
		}
		System.out.println();
	}
	public void reset() {
		symbols.clear();
		definedSymbols.clear();
		scopes.clear();
		scopeStack.push(instance);
	}
}
