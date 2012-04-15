package net.sangeeth.jssdk.tool;

import java.io.File;

public class JSCompiler {

	private static void error(String message) {
		System.err.println("Error: " + message);
		System.err.println();
		System.err.println("Usage:");
		System.err.println("java JSCompiler options sourceFile");
		System.err.println();
		System.err.println("Options");
		System.err.println("\t-d\tto specify the DWR descriptor location.");
		System.err.println("\t-v\tto specify the JSP folder.");
		System.err.println("\t-c\tto specify the context path of a Web Application.");
		System.err.println("\t-s\tto specify the source file path to be compiled");
		System.exit(1);
	}
		
	public static void main(String [] args) throws Exception {
		String dwrDescriptor = null;
		String destDirPath = null;
		String viewSrcDirPath = null;
		String contextPath = null;
		String sourceFilePath = null;
		
		for(String arg:args) {
			System.out.println(arg);
			if (arg.startsWith("-i")) {
				dwrDescriptor = arg.substring(2);
			} else if (arg.startsWith("-d")) {
				destDirPath = arg.substring(2);
			} else if (arg.startsWith("-v")) {
				viewSrcDirPath = arg.substring(2);;
			} else if (arg.startsWith("-c")) {
				contextPath = arg.substring(2);;
			} else {
				sourceFilePath = arg;
				break;
			}
		}
		
		if (dwrDescriptor == null) {
			error("lLease provide the DWR Descriptor path.");
		}

		if (contextPath==null) {
			error("Please provide the Context Path path of the Web Application.");
		}
		
		if (sourceFilePath==null) {
			error("Please provide the source file to be compiled.");
		}
		
		if (viewSrcDirPath==null) {
			error("Please provide the JSP source directory.");
		}

		
		File sourceFile = new File(sourceFilePath);
		
		File destDir;
		if (destDirPath==null)
		   destDir = sourceFile.getParentFile();
		else destDir = new File(destDirPath);
		
		Packager packager = new Packager();
		packager.setDWRDescriptor(dwrDescriptor);
		packager.setDestDir(destDir);
		packager.setViewSrcDir(new File(viewSrcDirPath));
		packager.setContextPath(contextPath);
		
		packager.expand(sourceFile);
	}
}
