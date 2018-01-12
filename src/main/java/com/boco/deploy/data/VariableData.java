package com.boco.deploy.data;

import java.util.Map;

public class VariableData {

	private Map<String, String> properties;;

	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	@Override
	public String toString() {
		return "VariableData [properties="+ properties + "]";
	}
	
	
}
