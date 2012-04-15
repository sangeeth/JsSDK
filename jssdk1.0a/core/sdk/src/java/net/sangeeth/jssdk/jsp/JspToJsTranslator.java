package net.sangeeth.jssdk.jsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Stack;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class JspToJsTranslator {
	
	private static final Logger logger = Logger.getLogger(JspToJsTranslator.class.getName());

	private static final String HEADER = "<c:code name=\"%s\" xmlns:c=\"http://java.sun.com/jstl/core_rt\">\n";
	private static final String FOOTER = "</c:code>";
	private static final String AMPERSAND_TAG = "<c:ampersand value=\"%s\"/>";
	
	private JspHandler jspHandler;
	private File workDir;

	public JspToJsTranslator() {
		workDir = new File("work");
		jspHandler = new JspHandler();
	}

	public File getWorkDir() {
		return workDir;
	}

	public void setWorkDir(File workDir) {
		this.workDir = workDir;
	}
	private String getName(File jspFile) {
		String name = jspFile.getName();
		name = name.replace('.','_');
		name = name.replace(' ','_');
		return name;
	}
	private File preprocess(File jspFile) throws Exception {
		if (!workDir.exists()) {
			workDir.mkdirs();
		}
		File preprocessedJspFile = new File(workDir, jspFile.getName());
		
		logger.info("Performing JSP preprocessing and saving output to " + preprocessedJspFile.getAbsolutePath());
		
		PrintWriter out = new PrintWriter(new FileWriter(preprocessedJspFile));
		
		out.printf(HEADER,getName(jspFile));
		
		StringBuffer entity = new StringBuffer();
		boolean entityRegion = false;
		BufferedReader in = new BufferedReader(new FileReader(jspFile));
		boolean scriptletRegion = false;
		Stack<Integer> chStack = new Stack<Integer>();
		int ch = -1;
		while ((ch = in.read()) != -1) {
			switch (ch) {
			case '<':
				if (!scriptletRegion) {
					chStack.push(ch);
					ch = in.read();
					if (ch == '%') {
						chStack.pop();
						scriptletRegion = true;
					}
				}
				break;
			case '%':
				if (scriptletRegion) {
					chStack.push(ch);
					ch = (char) in.read();
					if (ch == '>') {
						chStack.pop();
						scriptletRegion = false;
						ch = (char) in.read();
						if (ch == -1)
							break;
					}
				}
				break;
			case '&':
				entity.setLength(0);
				entityRegion = true;
				break;
			case ';':
				if (entityRegion) {
					out.printf(AMPERSAND_TAG,entity.toString());
					entityRegion = false;
					continue;
				}
				break;
			case '\r':
			case '\n':
				if (entityRegion) {
					out.print("&amp;");
					out.print(entity.toString());
					entityRegion = false;
				}
				break;
			}
			if (!scriptletRegion && !entityRegion) {
				while (!chStack.isEmpty()) {
					int c = chStack.pop();
					out.print((char) c);
				}
				out.print((char) ch);
			}
			if (entityRegion&&ch!='&') {
				entity.append((char)ch);
			}
		}

		out.println(FOOTER);
		
		in.close();
		out.close();

		return preprocessedJspFile;
	}

	public JspToJsTranslationResult translate(File jspFile, Writer out) throws Exception {
		JspToJsTranslationResult result = null;
		jspFile = preprocess(jspFile);
		
		try {
			// Obtain a new instance of a SAXParserFactory.
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// Specifies that the parser produced by this code will provide
			// support for XML namespaces.
			factory.setNamespaceAware(true);
			// Specifies that the parser produced by this code will validate
			// documents as they are parsed.
			factory.setValidating(true);
			// Creates a new instance of a SAXParser using the currently
			// configured factory parameters.
			SAXParser saxParser = factory.newSAXParser();
			
			
			// Collect Symbols
			
			jspHandler.setOut(new BlackHoleWriter());
			saxParser.parse(jspFile, jspHandler);
			
			Scope scope = Scope.getInstance();
			logger.info("Undefined symbols = " +scope.getAllUndefinedSymbols());
			
			jspHandler.setOut(out);
			saxParser.parse(jspFile, jspHandler);
			
			result = new JspToJsTranslationResult(this.getName(jspFile));
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return result;
	}	
	
	public JspToJsTranslationResult translate(File jspFile, File jsFile) throws Exception {
		JspToJsTranslationResult result = null;
		logger.info("Translating " + jspFile + " to " + jsFile);
		FileWriter out = new FileWriter(jsFile);
		result = this.translate(jspFile, out);
		out.close();
		logger.info("Finished Translating " + jspFile + " to " + jsFile);
		return result;
	}
}
