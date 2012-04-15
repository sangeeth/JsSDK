package net.sangeeth.jssdk.jsp;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public 	class JspHandler extends DefaultHandler {
	private static final String JSTL_CORE_NAMESPACE="http://java.sun.com/jstl/core_rt";
	
	private PrintWriter out;
	private TagHandlerManager tagHandlerManager;
	public JspHandler() {
		tagHandlerManager = TagHandlerManager.getInstance();
		
		out = new PrintWriter(new OutputStreamWriter(System.out));
	}
	public void setOut(Writer out) {
		this.out = new PrintWriter(out);
	}
	public Writer getOut() {
		return this.out;
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String text = ExpressionProcessor.process(new String(ch,start,length));
		if (text.length()>0) {
			out.println("html += \""+ text +"\";");
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (JSTL_CORE_NAMESPACE.equals(uri)) {
			TagHandler tagHandler = tagHandlerManager.getTagHandler(localName);
			if (tagHandler!=null)
				tagHandler.doEndTag(out);
		} else {
			out.println("html += \"</" + localName + ">\";");
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (JSTL_CORE_NAMESPACE.equals(uri)) {
			TagHandler tagHandler = tagHandlerManager.getTagHandler(localName);
			if (tagHandler!=null)
				tagHandler.doStartTag(out, attributes);
		} else {
			out.print("html += \"<" + localName);
			int n = attributes.getLength();
			for(int i=0;i<n;i++) {
				String name = attributes.getLocalName(i);
				String value = attributes.getValue(i);
				out.print(" " + name + "=\\\"" + ExpressionProcessor.process(value) + "\\\"");
			}
			out.println(">\";");
		}
	}
}
