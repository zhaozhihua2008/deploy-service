package com.boco.deploy.service;

public enum DeployType {
	install(1),uninstall(2);
	
	int value;
	private DeployType(int value) {
		this.value=value;
	}
	public int getValue() {
		return value;
	}
	
}
