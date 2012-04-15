package core;
import net.sangeeth.jssdk.jsp.Scope;


public class TestScope {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scope scope = Scope.getInstance();
		scope.addSymbol("message");
			scope.beginScope();
			scope.addSymbol("comment");
				scope.beginScope();
				scope.addSymbol("hello");
				scope.endScope();
			scope.endScope();
		scope.list();
	}

}
