package com.ecse321.team10.riderz.model;

/**
 * An object representing an user entry.
 * @version 1.0
 */
public class User {
	private String username;
	private String password;
	private String email;
	private String phone;
	private String firstName;
	private String lastName;

	public User(String username, String password, String email,
				String phone, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPhone() {
		return this.phone;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Obtains a String representation of this object.
	 * @return A String representing the object.
	 */
	public String toString() {
		return "[User] Username: " + this.username + " Password: " + this.password +
			   " Email: " + this.email + " Phone: " + this.phone + " First Name: " +
			   this.firstName + " Last Name: " + this.lastName;
	}
}
