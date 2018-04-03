package org.bitman.ay27.module.form;

public class LoginForm {
	private String email;
	private String password;

	public LoginForm() {
		// TODO Auto-generated constructor stub
	}

	public LoginForm(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginForm [email=" + email + ", password=" + password + "]";
	}

	
	

}
