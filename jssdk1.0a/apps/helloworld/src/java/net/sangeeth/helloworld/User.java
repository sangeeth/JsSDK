package net.sangeeth.helloworld;

public class User {
	private String name;
	private String emailId;
	private int age;
	
	public User() {
	}
	public User(String name, String emailId, int age) {
		this.name = name;
		this.emailId = emailId;
		this.age = age;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
