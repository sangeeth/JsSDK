package net.sangeeth.jssdk.jsp;

public class JspToJsTranslationResult {
	private String viewName;
	public JspToJsTranslationResult(String viewName) {
		this.viewName = viewName;
	}
	public String getViewName() {
		return viewName;
	}
	public String getFunctionName() {
		return "process_"+this.viewName+"_view";
	}
}
