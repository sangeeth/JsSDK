package net.sangeeth.blog;

public class LoginService {
	public void login(String user,String password) throws Exception {
		if (!("tintin".equals(user) && "test123".equals(password))) {
			throw new Exception("Authentication Failed. Tip: Try with 'tintin' as user and 'test123' as password.");
		}
	}
}
