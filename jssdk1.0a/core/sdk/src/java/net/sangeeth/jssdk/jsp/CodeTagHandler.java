package net.sangeeth.jssdk.jsp;

import java.io.PrintWriter;
import java.util.Set;

import org.xml.sax.Attributes;

public class CodeTagHandler implements TagHandler {
	private static final String FMT_FUNCTION_DECL="function process_%s_view(pageContext, " +
    "pageScope,requestScope,sessionScope,applicationScope," +
    "param,paramValues,header,headerValues,initParam,cookie) {" +
    "\nvar html = \"\";\n";
	private static final String FMT_VAR_DECL="var %s = pageContext[\"%s\"];";
	
	public void doEndTag(PrintWriter out) {
		out.println("return html;");
		out.println("}");
	}

	public void doStartTag(PrintWriter out,Attributes attributes) {
		out.printf(FMT_FUNCTION_DECL,attributes.getValue("name"));
		Scope scope = Scope.getInstance();
		Set<String> undefinedSymbols = scope.getAllUndefinedSymbols();
		if (!undefinedSymbols.isEmpty()) {
			for(String symbol:undefinedSymbols) {
				out.printf(FMT_VAR_DECL,symbol,symbol);
			}
			out.println();
		}
	}
}
