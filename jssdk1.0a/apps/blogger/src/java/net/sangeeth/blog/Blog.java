package net.sangeeth.blog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class Blog {
	private static List<Message> messages;
	static {
		messages = new ArrayList<Message>();
		
		Message message = new Message("JsSDK released!",
				   "JavaScript SDK gives a new dimension to AJAX development.",
				   "Sangeeth Kumar",
				   Calendar.getInstance().getTime()				
				);
		List<Comment> comments = new ArrayList<Comment>();
		comments.add(new Comment(Calendar.getInstance().getTime(),"Sangeeth","This is a sample comment"));
		message.setComments(comments);
		messages.add(message);
		message = new Message("Help Wanted!",
				   "Help me to develop JsSDK to support Tiles.",
				   "Sangeeth Kumar",
				   Calendar.getInstance().getTime()				
				);
		comments = new ArrayList<Comment>();
		comments.add(new Comment(Calendar.getInstance().getTime(),"Sangeeth","Need help to completely support JSTL"));
		message.setComments(comments);
		messages.add(message);
	}
	public Blog() {
	}
	public Message[] getMessages() {
		System.out.println("Returning messages " + this.messages);
		return this.messages.toArray(new Message[0]);
	}
	
	public void addMessage(Message message) {
		if (message!=null) {
			message.setTime(Calendar.getInstance().getTime());
			this.messages.add(message);
		}
	}
	public void removeMessage(String messageId) {
		for(Iterator<Message> i =messages.iterator();i.hasNext();) {
			Message p = i.next();
			if (p.getId().equals(messageId)){
				i.remove();
			}
		}
	}
	public Message getMessage(String messageId) {
		if (messageId==null) return null;
		System.out.println("messageid = " + messageId);
		for(Message message:messages) {
			System.out.println("message.getId() = " + message.getId());
			if (message.getId().equals(messageId))
				return message;
		}
		return null;
	}
	public void addComment(String messageId,
						String author,
						String email,
						String website,
						String body) {
		Message message = this.getMessage(messageId);
		if (message!=null) {
			Comment comment = new Comment();
			comment.setAuthor(author);
			comment.setBody(body);
			comment.setEmail(email);
			comment.setTime(Calendar.getInstance().getTime());
			comment.setWebsite(website);
			
			message.getComments().add(comment);
		}
	}
	public Message updateMessage(String user, 
						  String messageId, 
						  String title, 
						  String body) {
		Message message = this.getMessage(messageId);
		if (message==null) {
			message = new Message();
			message.setAuthor(user);
			this.addMessage(message);
		} 
		message.setBody(body);
		message.setTitle(title);
		
		return message;
	}
	public Message publish(String user,
			               String title,
			               String body) {
		return updateMessage(user, null, title, body);
	}
}
