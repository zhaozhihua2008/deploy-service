package com.boco.deploy.data;

public class PackageData {
	private String name;
	private String version;
	
	public String getId(){
		return name+"-"+version;
	}
}
