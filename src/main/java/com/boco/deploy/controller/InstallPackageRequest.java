package com.boco.deploy.controller;

import java.util.List;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.VariableData;

public class InstallPackageRequest {
	private String packageId;
	private List<HostData> hostDatas;
	private VariableData variableData;

	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public List<HostData> getHostDatas() {
		return hostDatas;
	}
	public void setHostDatas(List<HostData> hostDatas) {
		this.hostDatas = hostDatas;
	}
	public VariableData getVariableData() {
		return variableData;
	}
	public void setVariableData(VariableData variableData) {
		this.variableData = variableData;
	}
	
	
}
