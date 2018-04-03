package org.bitman.ay27.module.form;

public class RegisterForm {
	private String email;

	private String name;

	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RegisterForm() {

	}

	public RegisterForm(String email, String name, String password) {
		super();
		this.email = email;
		this.name = name;
		this.password = password;
	}

	@Override
	public String toString() {
		return "RegisterForm [email=" + email + ", name=" + name
				+ ", password=" + password + "]";
	}

	public boolean checkFieldValidation() {
		if ((this.email != null) && (!this.email.equals(""))
				&& (this.name != null) && (!this.name.equals(""))
				&& (this.password != null) && (!this.password.equals(""))) {
			return true;
		}
		return false;

	}

}
