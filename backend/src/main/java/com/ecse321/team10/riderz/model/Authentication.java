package com.ecse321.team10.riderz.model;

/**
 * An object representing a Session entry.
 * @version 1.0
 */
public class Authentication {
	private String username;
	private long sessionTime;
	private boolean active;

	public Authentication(String username, long sessionTime, boolean active) {
		this.username = username;
		this.sessionTime = sessionTime;
		this.active = active;
	}

	public String getUsername() {
		return this.username;
	}

	public long getSessionTime() {
		return this.sessionTime;
	}
	
	public boolean getActive() {
		return this.active;
	}
}
