package net.sangeeth.jssdk.jsp.el;

import java.util.Set;

public class ELResult {
	private String processedText;
	private Set<String> symbols;
	
	public String getProcessedText() {
		return processedText;
	}
	public void setProcessedText(String processedText) {
		this.processedText = processedText;
	}
	public Set<String> getSymbols() {
		return symbols;
	}
	public void setSymbols(Set<String> symbols) {
		this.symbols = symbols;
	}
	
}
