package net.sangeeth.jssdk.jsc;

import java.util.ArrayList;
import java.util.List;

public class DirectiveRef extends Symbol {
	private List<String> args;

	public DirectiveRef(String name) {
		super(name,SymbolType.DIRECTIVEDEF);
		this.args = new ArrayList<String>();
	}
	
	public List<String> getArgs() {
		return args;
	}

	public void setArgs(List<String> args) {
		this.args = args;
	}
}
