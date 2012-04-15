package net.sangeeth.jssdk.jsp.el;

abstract public class Symbol {
	protected String name;
	public Symbol(String name) {
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}
}
