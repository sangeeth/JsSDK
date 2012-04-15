package net.sangeeth.jssdk.jsc;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Scope {
	private String name;
	private Scope parent;
	private List<Symbol> symbolList;
	private List<Scope> scopeList;
	public Scope(String name,Scope parent) {
		this.name = name;
		this.symbolList = new ArrayList<Symbol>();
		this.scopeList = new ArrayList<Scope>();
		this.parent = parent;
	}
	public Scope(String name) {
		this(name,null);
	}
	public Scope() {
		this(null);
	}
	public Scope getParent() {
		return parent;
	}
	public void setParent(Scope parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Symbol> getSymbolList() {
		return symbolList;
	}
	public void setSymbolList(List<Symbol> symbolList) {
		this.symbolList = symbolList;
	}
	public List<Scope> getScopeList() {
		return scopeList;
	}
	public void setScopeList(List<Scope> scopeList) {
		this.scopeList = scopeList;
	}
	public void addSymbol(Symbol symbol) {
		this.symbolList.add(symbol);
	}
	public void addScope(Scope scope) {
		this.scopeList.add(scope);
	}
	public void list(OutputStream out) {
		PrintStream pout = new PrintStream(out);
		
		for(Symbol symbol:symbolList) {
			pout.println(symbol);
		}
	}
}
