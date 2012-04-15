package net.sangeeth.jssdk.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sangeeth.jssdk.jsp.JspToJsTranslationResult;
import net.sangeeth.jssdk.jsp.JspToJsTranslator;

public class ResourceLoader {
	private static final Logger logger = Logger.getLogger(ResourceLoader.class.getName());
	private final String FMT_VIEW_DECL="$import(\"js.ui.toolkit\");$export(\"%s\");" +
	"%s = new js.ui.toolkit.View(%s,%s);\n";
	private static final String SCRIPT_ROOT = "/net/sangeeth/jssdk/resource/script"; 
	private static final String CORE_SCRIPT_PREFIX="/";
	private static final String SCRIPT_PREFIX="/script";
	private static final String VIEW_PREFIX="/view";
	
	private File viewSrcDir;
	
	public ResourceLoader() {
		
	}
	public File getViewSrcDir() {
		return viewSrcDir;
	}
	public void setViewSrcDir(File viewSrcDir) {
		this.viewSrcDir = viewSrcDir;
	}
	public void returnScript(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String scriptPath = SCRIPT_ROOT;
		boolean coreScript = false;
		if (pathInfo==null
			||CORE_SCRIPT_PREFIX.equals(pathInfo)) {
			scriptPath+="/core.js";
			coreScript = true;
		} else {
			scriptPath+=pathInfo.substring(SCRIPT_PREFIX.length());
		}
		PrintWriter out = response.getWriter();
		
		InputStream scriptIn = this.getClass().getResourceAsStream(scriptPath);
		if (scriptIn!=null) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(scriptIn));
				String line = null;
				// TODO optimized file read and write
				while((line=in.readLine())!=null) {
					out.println(line);
				}
				out.flush();
				if(coreScript){
					out.printf("pageContext.request.contextPath=\"%s\";\n",request.getContextPath());
				}
			} catch (Exception e) {
				// TODO handle this exception appropriately
				throw new ServletException(e);
			}
		}
	}
	public void returnView(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		if (viewSrcDir!=null) {
			String viewPath = request.getParameter("viewPath");
			String viewId = request.getParameter("viewId");
			String viewEventHandler = request.getParameter("eventHandler");
			if (viewPath!=null && viewId!=null) {
				File viewFile = new File(viewSrcDir,viewPath);
				if (viewFile.exists()) {
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
					try {
						JspToJsTranslator translator = new JspToJsTranslator();
						JspToJsTranslationResult result = translator.translate(viewFile, out);
						out.printf(FMT_VIEW_DECL,viewId,viewId,result.getFunctionName(),viewEventHandler==null?"null":viewEventHandler);
					} catch (Exception e) {
						e.printStackTrace();
						throw new ServletException(e);
					} finally {
						out.flush();
					}
				} else {
					logger.warning("View not found " + viewPath);
				}
			}
		}		
	}	
}
