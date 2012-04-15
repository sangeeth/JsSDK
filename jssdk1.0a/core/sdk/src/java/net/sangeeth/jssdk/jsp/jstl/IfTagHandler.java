package net.sangeeth.jssdk.jsp.jstl;

import java.io.PrintWriter;

import org.xml.sax.Attributes;

import net.sangeeth.jssdk.jsp.ExpressionProcessor;
import net.sangeeth.jssdk.jsp.Scope;
import net.sangeeth.jssdk.jsp.TagHandler;
import net.sangeeth.jssdk.jsp.el.ELResult;

public class IfTagHandler implements TagHandler {
	private static final String FMT_IF_STATEMENT ="if(%s){\n";
	public void doEndTag(PrintWriter out) {
		out.println("}");
		
		Scope scope = Scope.getInstance();
		scope.endScope();
	}
	public void doStartTag(PrintWriter out, Attributes attributes) {
		String test = attributes.getValue("test");
		ELResult result = ExpressionProcessor.processEL(test);
		Scope scope = Scope.getInstance();
		scope.addSymbols(result.getSymbols());
		
		out.printf(FMT_IF_STATEMENT,result.getProcessedText());
		
		scope.beginScope();
	}
}
