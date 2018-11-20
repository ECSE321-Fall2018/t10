package com.ecse321.team10.riderz.dto;

public class AuthenticationDto {
	private String username;
	private long sessionTime;
	private boolean active;
	
	public AuthenticationDto() {}
	
	public AuthenticationDto(String username, long sessionTime, boolean active) {
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
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setSessionTime(long sessionTime) {
		this.sessionTime = sessionTime;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
}
