package core;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;

import net.sangeeth.jssdk.jsc.CodeCompressor;
import net.sangeeth.jssdk.jsc.JSParser;
import net.sangeeth.jssdk.jsc.SymbolTableBuilder;


public class TestJSParserListener {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
//		File file = new File("C:/sangeeth/works/eclipse/personal/Blogger/WebContent/scripts/blogger.js");
		File file = new File("test.js");
//		Writer writer = new OutputStreamWriter(System.out);
//		parser.addParserListener(new CodeCompressor(writer));		
		JSParser parser = new JSParser();
		parser.parse(file);
	}
}
