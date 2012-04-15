package net.sangeeth.jssdk.jsp;

import net.sangeeth.jssdk.jsp.el.ELProcessor;
import net.sangeeth.jssdk.jsp.el.ELResult;

public class ExpressionProcessor {
	private static ELProcessor elProcessor;
	private static StringBuffer exp;
	private static StringBuffer jstlExp;
	
	static {
		elProcessor = new ELProcessor();
		exp = new StringBuffer();
		jstlExp = new StringBuffer();
	}
	public static ELResult processEL(String el) {
		ELResult result = null;
		if (el.startsWith("${") && el.endsWith("}")){
			el = el.substring(2,el.length()-1);
			result = elProcessor.parse(el);
		} 
		return result;
	}
	public static String process(String text) {
		int n = text.length();
		exp.setLength(0);
		jstlExp.setLength(0);
		
		char ch;
		boolean el = false;
		for(int i=0;i<n;i++) {
			ch = text.charAt(i);
			switch(ch) {
			case '\r':
			case '\n':
				//consume
				continue;
			case '$':
				if (text.charAt(i+1)=='{') {
					jstlExp.setLength(0);
					el = true;
					i++;
					exp.append("\" + (");
					continue;
				}
				break;
			case '}':
				if (el) {
					String e = jstlExp.toString();
					ELResult result = elProcessor.parse(e);
					Scope scope = Scope.getInstance();
					scope.addSymbols(result.getSymbols());
					exp.append(result.getProcessedText());
					exp.append(") + \"");
					el = false;
					continue;
				}
				break;
			case '"':
				if (!el) {
					exp.insert(i, '\\');
					i++;
				}
			}
			if (el) {
				jstlExp.append(ch);
			} else {
				exp.append(ch);
			}
		}
		return exp.toString();
	}
}
