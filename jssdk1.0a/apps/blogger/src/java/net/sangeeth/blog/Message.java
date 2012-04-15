package net.sangeeth.blog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Message {
	private String id;
	private String title;
	private String body;
	private String author;
	private Date time;
	private List<Comment> comments;
	private static final Random random = new Random(); 
	public Message() {
		this(null,null,null,null,new ArrayList<Comment>());
	}
	public Message(String title, String body, String author, Date time, List<Comment> comments) {
		super();
		this.id = "Pid"+System.currentTimeMillis() + random.nextInt();
		this.title = title;
		this.body = body;
		this.author = author;
		this.time = time;
		this.comments = comments;
	}
	public Message(String title, String body, String author, Date time) {
		this(title,body,author,time,null);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String toString() {
		return "Message[id=" + id +", title=" + title + ", body=" + body + ", author=" + author +"]"; 
	}
}
