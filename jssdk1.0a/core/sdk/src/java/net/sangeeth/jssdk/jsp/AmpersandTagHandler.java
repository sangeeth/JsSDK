package net.sangeeth.jssdk.jsp;

import java.io.PrintWriter;

import org.xml.sax.Attributes;

public class AmpersandTagHandler implements TagHandler {
	private static final String FMT_AMPERSAND="html += \"&%s;\";\n";

	public void doEndTag(PrintWriter out) {
	}

	public void doStartTag(PrintWriter out,Attributes attributes) {
		out.printf(FMT_AMPERSAND,attributes.getValue("value"));
	}
}
