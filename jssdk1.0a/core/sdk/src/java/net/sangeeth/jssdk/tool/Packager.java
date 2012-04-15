package net.sangeeth.jssdk.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import uk.ltd.getahead.dwr.AccessControl;
import uk.ltd.getahead.dwr.Configuration;
import uk.ltd.getahead.dwr.ConverterManager;
import uk.ltd.getahead.dwr.CreatorManager;
import uk.ltd.getahead.dwr.Processor;
import uk.ltd.getahead.dwr.WebContextBuilder;
import uk.ltd.getahead.dwr.impl.DefaultConfiguration;
import uk.ltd.getahead.dwr.impl.DefaultContainer;

import net.sangeeth.jssdk.jsc.Directive;
import net.sangeeth.jssdk.jsc.DirectiveRef;
import net.sangeeth.jssdk.jsc.JSParser;
import net.sangeeth.jssdk.jsc.JSParserAdapter;
import net.sangeeth.jssdk.jsc.Keyword;
import net.sangeeth.jssdk.jsc.Separator;
import net.sangeeth.jssdk.jsc.Symbol;
import net.sangeeth.jssdk.jsc.Token;
import net.sangeeth.jssdk.jsc.TokenType;
import net.sangeeth.jssdk.util.ResourceLoader;

public class Packager extends JSParserAdapter {
	private static final Logger logger = Logger.getLogger(Packager.class.getName());
	
	private PrintWriter writer;
	private boolean directiveCompleted;
	
	private DefaultContainer dwrContainer;
	private Processor dwrProcessor;
	private ResourceLoader resourceLoader;
	
	private File viewSrcDir;
	private File workDir;
	private File destDir;
	private String include;
	private String contextPath;
	private String dwrServletPath;
	private String jssdkServletPath;
	
	public static final String DEFAULT_DWR_SERVLET_PATH = "/dwr";
	public static final String DEFAULT_JSSDK_SERVLET_PATH = "/jssdk";
	
	private List<String> importedViews;
	private List<String> importedAssemblies;
	private List<String> importedInterfaces;
	private List<String> definedPackages;
	private List<String> definedClasses;
	
	public Packager() {
		this.importedViews = new ArrayList<String>();
		this.importedAssemblies = new ArrayList<String>();
		this.importedInterfaces = new ArrayList<String>();
		this.definedPackages = new ArrayList<String>();
		this.definedClasses = new ArrayList<String>();
		
		resourceLoader = new ResourceLoader();
		
		this.dwrServletPath = DEFAULT_DWR_SERVLET_PATH;
		this.jssdkServletPath = DEFAULT_JSSDK_SERVLET_PATH;
		
		try {
			initDWR();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		this.workDir = new File("work");
	}
	
	public File getWorkDir() {
		return workDir;
	}

	public void setWorkDir(File workDir) {
		this.workDir = workDir;
	}

	public String getJssdkServletPath() {
		return jssdkServletPath;
	}

	public void setJssdkServletPath(String jssdkServletPath) {
		this.jssdkServletPath = jssdkServletPath;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public File getDestDir() {
		return destDir;
	}

	public void setDestDir(File destDir) {
		this.destDir = destDir;
	}

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	public String getDwrServletPath() {
		return dwrServletPath;
	}

	public void setDwrServletPath(String dwrServletPath) {
		this.dwrServletPath = dwrServletPath;
	}

	public File getViewSrcDir() {
		return viewSrcDir;
	}

	public void setViewSrcDir(File viewSrcDir) {
		this.viewSrcDir = viewSrcDir;
		resourceLoader.setViewSrcDir(viewSrcDir);		
	}
	
	public void setDWRDescriptor(String descriptor) throws Exception {
		InputStream in = null;
		File file = new File(descriptor);
		if (file.exists()) {
			in = new FileInputStream(file);
		} else { 
		   in = this.getClass().getResourceAsStream(descriptor);
		}	
		if (in !=null) {
			DefaultConfiguration configuration = (DefaultConfiguration) dwrContainer.getBean(Configuration.class.getName());
	        configuration.addConfig(in);
		}
	}

	protected void initDWR() throws Exception {
		dwrContainer = new DefaultContainer();
		
		dwrContainer.addParameter(AccessControl.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultAccessControl"); //$NON-NLS-1$
		dwrContainer.addParameter(Configuration.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultConfiguration"); //$NON-NLS-1$
		dwrContainer.addParameter(ConverterManager.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultConverterManager"); //$NON-NLS-1$
		dwrContainer.addParameter(CreatorManager.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultCreatorManager"); //$NON-NLS-1$
		dwrContainer.addParameter(Processor.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultProcessor"); //$NON-NLS-1$
		dwrContainer.addParameter(WebContextBuilder.class.getName(), "uk.ltd.getahead.dwr.impl.DefaultWebContextBuilder"); //$NON-NLS-1$

        dwrContainer.addParameter("interface", "net.sangeeth.jssdk.dwr.DefaultInterfaceProcessor");
        dwrContainer.addParameter("exec", "net.sangeeth.jssdk.dwr.DefaultExecProcessor");
        dwrContainer.addParameter("debug", "false"); //$NON-NLS-1$ //$NON-NLS-2$
        dwrContainer.addParameter("scriptCompressed", "true"); //$NON-NLS-1$ //$NON-NLS-2$
		
        dwrContainer.configurationFinished();
        
        this.dwrProcessor = (Processor) dwrContainer.getBean(Processor.class.getName());
		
        DefaultConfiguration configuration = (DefaultConfiguration) dwrContainer.getBean(Configuration.class.getName());
        
        InputStream in = this.getClass().getResourceAsStream("/uk/ltd/getahead/dwr/dwr.xml");
        configuration.addConfig(in);
	}	
	protected void definePackage(String packageName) {
		if (!this.definedPackages.contains(packageName)) {
			
			String[] names = packageName.split("\\.");
			if (names.length>1) {
				String basePkg = names[0];
				for(int i=1;i<names.length;i++) {
					basePkg+=".";
					basePkg+=names[i];
					if (!this.definedPackages.contains(basePkg)) {
						this.definedPackages.add(basePkg);
						//System.out.println("Defined pkg " + basePkg + " this.definedPackages =" +this.definedPackages);
						this.writer.printf("%s = function(){};",basePkg);		
					}
				}
			}
		}
	}
	protected void exportClass(String className) {
		if (!this.definedClasses.contains(className)) {
			int lastDot = className.lastIndexOf('.');
			String packageName = className.substring(0,lastDot);
			
			definePackage(packageName);
			
			this.writer.printf("%s = function(){};",className);
			
			this.definedClasses.add(className);
		}
	}
	public void importInterface(String interfaceName) throws Exception {
		if (!this.importedInterfaces.contains(interfaceName)) {

			MutableHttpServletRequest request = new MutableHttpServletRequest();
			MutableHttpServletResponse response = new MutableHttpServletResponse();
			request.setContextPath(contextPath);
			
			File tempFile = new File(workDir,"interface_"+interfaceName.replace('.','_')+".js");
			PrintWriter tempWriter = new PrintWriter(new FileWriter(tempFile));
			response.setWriter(tempWriter);
			request.setServletPath(dwrServletPath);
			request.setPathInfo("/interface/" + interfaceName);
			this.dwrProcessor.handle(request, response);
			
			tempWriter.close();
			this.expand(tempFile,this.writer);
			
			this.importedInterfaces.add(interfaceName);
		}
	}
	public void importAssembly(String assembly) throws Exception {
		if (!this.importedAssemblies.contains(assembly)) {
			this.importedAssemblies.add(assembly);
			
			MutableHttpServletRequest request = new MutableHttpServletRequest();
			MutableHttpServletResponse response = new MutableHttpServletResponse();
			request.setContextPath(contextPath);
			
			File tempFile = new File(workDir,"assembly_"+assembly.replace('.','_')+".js");
			PrintWriter tempWriter = new PrintWriter(new FileWriter(tempFile));
			response.setWriter(tempWriter);
			
			assembly = assembly.replace('.','/');
			assembly+=".js";
			request.setPathInfo("/script/"+assembly);
			resourceLoader.returnScript(request, response);
			
			tempWriter.close();
			this.expand(tempFile,this.writer);
		}
	}
	public void importView(String viewId,String viewPath,String eventHandler) throws Exception {
		if (!this.importedViews.contains(viewPath)) {
			logger.info("Importing View with ViewId: " + viewId + " viewPath: " + viewPath + " eventHandler: " + eventHandler);
			
			MutableHttpServletRequest request = new MutableHttpServletRequest();
			MutableHttpServletResponse response = new MutableHttpServletResponse();
			request.setContextPath(contextPath);
			
			File tempFile = new File(workDir,"view_"+viewPath.replace('.','_')+".js");
			PrintWriter tempWriter = new PrintWriter(new FileWriter(tempFile));
			response.setWriter(tempWriter);

			request.setServletPath(jssdkServletPath);
			request.setPathInfo("/view");
			//viewPath="+path+"&viewId="+viewId+"&eventHandler="+eventHandler
			request.setParameter("viewPath", viewPath);
			request.setParameter("viewId", viewId);
			request.setParameter("eventHandler", eventHandler);
			
			resourceLoader.returnView(request, response);
			
			tempWriter.close();
			this.expand(tempFile,this.writer);
			
			this.importedViews.add(viewPath);
		}
	}
		
	protected void expand(File script,PrintWriter writer) throws Exception {
		directiveCompleted = true;
		JSParser parser = new JSParser();
		parser.addParserListener(this);
		parser.parse(script);
		parser.removedParserListener(this);
	}

	public void expand(File script) throws Exception {
		if (this.destDir==null) {
			this.destDir = script.getParentFile();
		}
		if (!this.workDir.exists()) {
			this.workDir.mkdirs();
		}
		File objectCode = new File(this.destDir,script.getName().replace(".js",".jso"));
		PrintWriter writer = new PrintWriter(new FileWriter(objectCode));
		this.writer = writer;
		this.expand(script,writer);
		writer.printf("\ndocument.cookie=\"scriptcompiled=1;path=/\";");
		writer.close();
	}

	
	// JSParserListener Methods
	public void beginBlock() {
		writer.println();
	}
	public void consumedToken(Token token) {
		TokenType tokenType = token.getType();
		if (tokenType == TokenType.DIRECTIVE) {
			directiveCompleted = false;
		} 
		
		if (directiveCompleted) {
			if (token == Keyword.IN
				|| token == Keyword.INSTANCEOF
				|| token == Keyword.TYPEOF) {
				writer.print(' ');
			}
			
			writer.print(token);
			if (token.getType()==TokenType.KEYWORD) {
				writer.print(' ');
			}
			else if (token == Separator.SEMICOLON) {
				writer.println();
			}
		}
		writer.flush();
	}

	public void definedSymbol(Symbol symbol) {
		// TODO Auto-generated method stub
		
	}

	private String deliteral(String stringLiteral) {
		return stringLiteral.substring(1,stringLiteral.length()-1);
	}
	
	public void declaredDirective(DirectiveRef directiveRef) {
		
		String name = directiveRef.getName();
		directiveCompleted = true;
		
		if (name.equals(Directive.VIEW_IMPORT_DIRECTIVE.getValue())) {
			List<String> args = directiveRef.getArgs();
			if (args.size()>=2){
				String eventHandler = (args.size()==3)?deliteral(args.get(2)):null;
				try {
					importView(deliteral(args.get(0)), deliteral(args.get(1)), eventHandler);
				} catch (Exception e) {
					logger.warning("Error while including view. Error:" + e.getMessage());
				}
			}
		} else if (name.equals(Directive.ASSEMBLY_IMPORT_DIRECTIVE.getValue())) {
			List<String> args = directiveRef.getArgs();
			if (!args.isEmpty()) {
				String assembly = deliteral(args.get(0));
				try {
					importAssembly(assembly);
				} catch (Exception e) {
					logger.warning("Error while including assembly. Error:" + e.getMessage());
				}
			}
		} else if (name.equals(Directive.SERVICE_IMPORT_DIRECTIVE.getValue())) {
			List<String> args = directiveRef.getArgs();
			if (!args.isEmpty()) {
				String interfaceName = deliteral(args.get(0));
				try {
					importInterface(interfaceName);
				} catch (Exception e) {
					logger.warning("Error while including service. Error:" + e.getMessage());
				}
			}		
		} else if (name.equals(Directive.CLASS_EXPORT_DIRECTIVE.getValue())) {
			List<String> args = directiveRef.getArgs();
			if (!args.isEmpty()) {
				String className = deliteral(args.get(0));
				try {
					exportClass(className);
				} catch (Exception e) {
					logger.warning("Error while including service. Error:" + e.getMessage());
				}
			}		
		}
		
	}

	public void endBlock() {
		writer.println();
	}

	public void finished(boolean success) {
	}

	public void started() {
		// TODO Auto-generated method stub
		
	}	
	
	public static void main(String [] arg) throws Exception {
		Packager packager = new Packager();
		packager.setDWRDescriptor("/dwr.xml");
		packager.setDestDir(new File("C:/sangeeth/bin/apache-tomcat-5.5.17/webapps/blogger1.0/scripts"));
		packager.setViewSrcDir(new File("C:\\sangeeth\\works\\eclipse\\personal\\Blogger\\WebContent"));
		packager.setContextPath("/blogger1.0");
		
		File file = new File("C:/sangeeth/works/eclipse/personal/Blogger/WebContent/scripts/blogger.js");
		packager.expand(file);
	}
}