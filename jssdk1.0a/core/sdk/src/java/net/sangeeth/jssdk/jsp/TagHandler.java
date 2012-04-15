package net.sangeeth.jssdk.jsp;

import java.io.PrintWriter;

import org.xml.sax.Attributes;

public interface TagHandler {
	public void doStartTag(PrintWriter out,Attributes attributes);
	public void doEndTag(PrintWriter out);
}
