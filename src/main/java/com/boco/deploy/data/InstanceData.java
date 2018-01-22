package com.boco.deploy.data;

public class InstanceData {
	private String instanceName;
	private PackageData packageData;
	private HostData hostData;
	private VariableData variableData;
	
	
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public PackageData getPackageData() {
		return packageData;
	}
	public void setPackageData(PackageData packageData) {
		this.packageData = packageData;
	}
	public HostData getHostData() {
		return hostData;
	}
	public void setHostData(HostData hostData) {
		this.hostData = hostData;
	}
	public VariableData getVariableData() {
		return variableData;
	}
	public void setVariableData(VariableData variableData) {
		this.variableData = variableData;
	}
	
	
}
