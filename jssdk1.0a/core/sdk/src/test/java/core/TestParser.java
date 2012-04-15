package core;
import java.io.File;
import java.io.FileReader;

import net.sangeeth.jssdk.jsc.JSParser;
import net.sangeeth.jssdk.jsc.JSLexer;
import net.sangeeth.jssdk.jsc.JSParserAdapter;
import net.sangeeth.jssdk.jsc.Token;

import net.sangeeth.jssdk.jsc.Symbol;

public class TestParser {
	static JSParser parser = new JSParser();
	public static void main(String [] arg) throws Exception {
//		tree(new File("C:/sangeeth/works/eclipse/personal/Blogger/WebContent/scripts/js/"));
		
		File file = new File("test.js");
		parser.setWarningEnabled(true);
		parser.addParserListener(new JSParserAdapter() {

			@Override
			public void consumedToken(Token token) {
				System.out.println(token);
			}
			
		});
		if (parser.parse(file)) {
			System.out.println("Compiled");
		}
	}
	static StringBuffer padding = new StringBuffer();
	public static void tree(File file) {
		if (file.isDirectory()) {
			//System.out.println(padding.toString() + file.getName());
			padding.append(".");
			for(File f:file.listFiles()) {
				tree(f);
			}
			padding.deleteCharAt(0);
		} else {
			if (file.getName().endsWith(".js")) {
				try {
					if (!parser.parse(file)) {
						System.out.println(file.getPath().replace('\\', '/'));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
