package net.sangeeth.blog;

import java.util.Date;

public class Comment {
	private Date time;
	private String author;
	private String body;
	private String email;
	private String website;
	
	public Comment() {

	}
	
	public Comment(Date time, String author, String body) {
		super();
		this.time = time;
		this.author = author;
		this.body = body;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
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
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
