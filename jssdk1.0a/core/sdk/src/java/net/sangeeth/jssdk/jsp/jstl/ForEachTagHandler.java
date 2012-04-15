package net.sangeeth.jssdk.jsp.jstl;

import java.io.PrintWriter;
import java.text.MessageFormat;

import org.xml.sax.Attributes;

import net.sangeeth.jssdk.jsp.ExpressionProcessor;
import net.sangeeth.jssdk.jsp.Scope;
import net.sangeeth.jssdk.jsp.TagHandler;
import net.sangeeth.jssdk.jsp.el.ELResult;

public class ForEachTagHandler implements TagHandler {
//	var post;
//	for(var i in pageContext.previousPosts) {
//	post = pageContext.previousPosts[i];

	private static int indexCount=1;
	private static final String FMT_FOR_STATEMENT = "var {0}; var {1} = {2};"+
													"for(var {3} in {1}) '{'\n" +
													"{0} = {1}[{3}];\n";
	public ForEachTagHandler() {
	}
	public void doEndTag(PrintWriter out) {
		out.println("}");
		
		Scope scope = Scope.getInstance();
		scope.endScope();
	}

	public void doStartTag(PrintWriter out,Attributes attributes) {
		String items = attributes.getValue("items");
		String var = attributes.getValue("var");
		ELResult result = ExpressionProcessor.processEL(items);
		
		Scope scope = Scope.getInstance();
		scope.addSymbols(result.getSymbols());
		scope.beginScope();
		scope.defineSymbol(var);
		String index = "i"+String.valueOf(indexCount++);
		String source = result.getProcessedText();
		String sourceVar = var + "_src";
		out.print(MessageFormat.format(FMT_FOR_STATEMENT,var,sourceVar,source,index));		
	}
}
