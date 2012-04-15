package net.sangeeth.jssdk.jsc;

public class Symbol {
	private SymbolType symbolType;
	private String name;
	private Symbol dataType;
	public Symbol(String name,SymbolType symbolType) {
		this.name = name;
		this.symbolType = symbolType;
	}
	public String getName() {
		return name;
	}
	public SymbolType getSymbolType() {
		return symbolType;
	}
	
	public Symbol getDataType() {
		return dataType;
	}
	public void setDataType(Symbol dataType) {
		this.dataType = dataType;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSymbolType(SymbolType symbolType) {
		this.symbolType = symbolType;
	}
	public String toString() {
		return symbolType + ": " + name;
	}
}
