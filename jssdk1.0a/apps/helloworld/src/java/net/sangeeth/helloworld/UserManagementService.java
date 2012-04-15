package net.sangeeth.helloworld;

import java.util.ArrayList;
import java.util.List;

public class UserManagementService {
	private static List<User> users;
	static {
		users = new ArrayList<User>();
		users.add(new User("sangeeth","sangeeth.kumar@gmail.com",29));
		users.add(new User("tintin","tintin@gmail.com",35));
		users.add(new User("priya","priya@msn.com",26));
		users.add(new User("gaja","gaja@yahoo.com",26));
		users.add(new User("srini","srini@oracle.com",26));
	}

	public UserManagementService() {
	}
	public User[] getUsers() {
		return users.toArray(new User[0]);
	}
	public void addUser(User user) {
		this.users.add(user);
	}
	public void removeUser(User user) {
		this.users.remove(user);
	}
}
