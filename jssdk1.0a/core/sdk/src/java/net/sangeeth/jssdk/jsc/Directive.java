package net.sangeeth.jssdk.jsc;

import java.util.Map;

public class Directive extends Token {
	public static final Directive ASSEMBLY_IMPORT_DIRECTIVE = new Directive("$import");
	public static final Directive VIEW_IMPORT_DIRECTIVE = new Directive("$importView");
	public static final Directive SERVICE_IMPORT_DIRECTIVE = new Directive("$importInterface");
	public static final Directive CLASS_EXPORT_DIRECTIVE = new Directive("$export");
	
	private static final Map<String,Directive> cache = Token.getTokenMap(Directive.class);
	private Directive(String value) {
		super(value, TokenType.DIRECTIVE);
	}
	public static Directive valueOf(String token) {
		return cache.get(token);
	}
	
	
}
