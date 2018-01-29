package com.boco.deploy.data;

public class HostData {
	private String ip;
	private String userName;
	private String password;
	private boolean sudo;
	private String sudoPass;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isSudo() {
		return sudo;
	}
	public void setSudo(boolean sudo) {
		this.sudo = sudo;
	}
	public String getSudoPass() {
		return sudoPass;
	}
	public void setSudoPass(String sudoPass) {
		this.sudoPass = sudoPass;
	}
	@Override
	public String toString() {
		return "HostData [ip=" + ip + ", userName=" + userName + ", password="
				+ password + ", sudo=" + sudo + ", sudoPass=" + sudoPass + "]";
	}

	
}
