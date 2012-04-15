package net.sangeeth.jssdk.jsp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class TagHandlerManager {
	private static TagHandlerManager instance = null;
	public static TagHandlerManager getInstance() {
		if (instance == null ) {
			instance = new TagHandlerManager();
			instance.load();
		}
		return instance;
	}
	private Map<String,TagHandler> registry;
	private TagHandlerManager() {
		registry = new HashMap<String,TagHandler>();
	}
	protected void load() {
		Properties props = new Properties();
		try {
			props.load(this.getClass().getResourceAsStream("taghandlers.map"));
			Iterator i = props.entrySet().iterator();
			for(;i.hasNext();) {
				Map.Entry entry = (Map.Entry)i.next();
				String tag = (String)entry.getKey();
				String handlerClass = (String)entry.getValue();
				try {
					Class clazz = Class.forName(handlerClass);
					addTagHandler(tag, (TagHandler)clazz.newInstance());
				} catch (Exception e) {
					System.err.println("Failed to register handler " + handlerClass + " for tag " + tag);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addTagHandler(String tag,TagHandler tagHandler) {
		registry.put(tag, tagHandler);
	}
	public void removeTagHandler(String tag) {
		registry.remove(tag);
	}
	public TagHandler getTagHandler(String tag) {
		return registry.get(tag);
	}
}
