package com.boco.deploy.data;

public class PackageData {
	private String name;
	private String version;
	
	public String getId(){
		return name+"_"+version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
