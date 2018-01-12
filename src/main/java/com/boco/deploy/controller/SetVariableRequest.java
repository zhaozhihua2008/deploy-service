package com.boco.deploy.controller;

import com.boco.deploy.data.VariableData;

public class SetVariableRequest {
	private String packageId;
	private VariableData variableData;
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public VariableData getVariableData() {
		return variableData;
	}
	public void setVariableData(VariableData variableData) {
		this.variableData = variableData;
	}

	
}
