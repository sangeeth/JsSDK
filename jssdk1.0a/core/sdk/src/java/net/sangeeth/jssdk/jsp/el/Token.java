package net.sangeeth.jssdk.jsp.el;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Token {
	protected String value;
	protected TokenType type;
	
	protected static <A extends Token> Map<String,A> getTokenMap(Class<A> type){
		HashMap<String,A> map = new HashMap<String,A>();
		try {
			Field [] fields = type.getFields();
			for(Field field:fields) {
				if (field.getType().equals(type)) {
					A v = (A)field.get(null);
					map.put(v.value, v);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return map;
	}
	
	public Token(String value,TokenType type) {
		this.value = value;
		this.type = type;
	}

	public TokenType getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
	
	public String toString() {
		return this.value;
	}
	public boolean equals(Object v) {
		boolean b = false;
		if (v instanceof String) {
			b = value.equals(v);
		} else if (v instanceof Token) {
			b = value.equals(((Token)v).value);
		}
		return b;
	}
}
