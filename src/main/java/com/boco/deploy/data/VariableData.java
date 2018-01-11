package com.boco.deploy.data;

import java.util.Properties;

public class VariableData {

	String packageId;
	private Properties properties;
	
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	@Override
	public String toString() {
		return "VariableData [packageId=" + packageId + ", properties="
				+ properties + "]";
	}
	
	
}
