package core;
import java.text.MessageFormat;


public class Test {
	private static final String FMT_FOR_STATEMENT = "var {0};\n"+
													"for(var {1} in {2}) '{'\n" +
													"{0} = {2}[{1}];";
	public static void main(String[] args) {
		System.out.println("\n".replace("\\","\\\\"));
	}
}
