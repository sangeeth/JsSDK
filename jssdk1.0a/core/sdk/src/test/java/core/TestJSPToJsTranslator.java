package core;
import java.io.File;
import java.io.FilenameFilter;

import net.sangeeth.jssdk.jsp.JspToJsTranslator;

public class TestJSPToJsTranslator {
	public static void main(String[] args) throws Exception {
		File srcdir = new File("C:\\sangeeth\\works\\eclipse\\personal\\Blogger\\WebContent");
		File [] viewFiles = srcdir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {
				return (name.endsWith(".jsp") && name.startsWith("snippet_"));
			}
		});
		
		JspToJsTranslator translator = new JspToJsTranslator();
		//for(File viewFile:viewFiles) {
		{
			File viewFile = new File("C:\\sangeeth\\works\\eclipse\\personal\\Blogger\\WebContent\\snippet_login_form.jsp");
			File jsFile = new File("c:\\sangeeth\\temp\\snippet\\" + viewFile.getName().replace(".jsp", ".js"));
			translator.translate(viewFile, jsFile);
		}
//		File jspFile = new File("C:\\sangeeth\\works\\eclipse\\personal\\Blogger\\WebContent\\comments.jsp");
	}
}
