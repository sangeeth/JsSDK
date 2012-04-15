package core;
import net.sangeeth.jssdk.jsp.el.ELProcessor;
import net.sangeeth.jssdk.jsp.el.ELResult;


public class TestELProcessor {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ELProcessor elprocessor = new ELProcessor();
		System.out.println(elprocessor.process("post.id"));
		System.out.println(elprocessor.process("post.title == '10'"));
		System.out.println(elprocessor.process("(post.title == '10')?true:((empty param.name)?true:false)"));
		
		ELResult result = elprocessor.parse("(post.title == '10')?comment.length:((empty param.name)?true:false)");
		System.out.println(result.getProcessedText() + " " + result.getSymbols());
		
//		Token token = null;
//		while((token = elprocessor.nextToken())!=null) {
//			System.out.println(token);
//		}
	}
}
