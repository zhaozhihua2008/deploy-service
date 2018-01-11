package com.boco.deploy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.InstallLogData;
import com.boco.deploy.data.PackageData;
import com.boco.deploy.data.VariableData;

@Service
public class DeployServiceImpl implements DeployService {

	@Override
	public List<PackageData> getAllPackage(String exp) {
		PackageData data=new PackageData();
		data.setName("k8s");
		data.setVersion("v1.7.5");
		List<PackageData> list=new ArrayList<PackageData>();
		list.add(data);
		return list;
	}

	@Override
	public VariableData getVariable(String packageId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setVariable(String packageId, VariableData variableData) {
		System.out.println(variableData);
		return true;
	}

	@Override
	public String installPackage(String packageId, List<HostData> hostDatas,
			VariableData variableData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uninstallPackage(String packageId, List<HostData> hostDatas,
			VariableData variableData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstallLogData getInstallLog(String logId, int startIndex,
			int lineNum) {
		// TODO Auto-generated method stub
		return null;
	}

}
