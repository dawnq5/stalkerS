package com.d.stalker.bean;

import java.util.Date;

public class Message {
	private String formUser;
	private String toUser;
	private Date time;
	private String message;
	private Location location;
	private String cmd;
	public String getFormUser() {
		return formUser;
	}
	public void setFormUser(String formUser) {
		this.formUser = formUser;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	

}
