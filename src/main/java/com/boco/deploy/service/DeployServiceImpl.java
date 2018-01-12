package com.boco.deploy.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.InstallLogData;
import com.boco.deploy.data.PackageData;
import com.boco.deploy.data.VariableData;
import com.boco.deploy.util.Constant;
import com.boco.deploy.util.StringUtil;


@Service
public class DeployServiceImpl implements DeployService {
	private static Logger logger = LoggerFactory.getLogger(DeployServiceImpl.class);

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
			VariableData variableData) throws Exception {
		String timestamp=StringUtil.getTimeStamp();
		//write hosts file
		String hostsFileName=createHostsFile(packageId, hostDatas, variableData, timestamp);
		
		//create temp shell
		String opName="install";
		createTempShell(opName, packageId, variableData, timestamp);
		createTempShell("uninstall", packageId, variableData, timestamp);
		
		//exec command
		String logPath=Constant.DIR_APP_LOG+packageId+"-"+opName+"-"+timestamp+".log";;
		String cmd="ansible-playbook  -i "+hostsFileName+" "+opName+".yml --extra-vars \"package_id="+packageId+",timestamp="+timestamp+"\" -v ";
		File logFile=new File(logPath);
		FileUtils.forceMkdirParent(logFile);
		ShellExecutor shellExecutor=new ShellExecutor(cmd,logFile);
		shellExecutor.start();
		return logPath;
	}

	/**
	 * 根据hostDatas生成ansible hosts文件
	 */
	private String createHostsFile(String packageId, List<HostData> hostDatas,
			VariableData variableData,String timestamp) throws Exception {
		//write hosts file
		String firstHost=hostDatas.get(0).getIp();
		String hostsFileName=firstHost+"-"+timestamp+".hosts";
		String hostsFilePath=Constant.DIR_APP_PROJECT+packageId+"/"+hostsFileName;
		File hostsFile=new File(hostsFilePath);
		List<String> hostsLines=new ArrayList<String>();
		for (HostData hostData : hostDatas) {
			String line=hostData.getIp()+" ansible_ssh_user="+hostData.getUserName()+" ansible_ssh_pass="+hostData.getPassword();
			if (hostData.isSudo()) {
				line=line+" ansible_sudo_pass="+hostData.getSudoPass()+" ansible_become=true";
			}
			hostsLines.add(line);
		}
		FileUtils.writeLines(hostsFile, hostsLines);
		return hostsFileName;
	}
	
	/**
	 * 根据变量创建临时脚本
	 */
	private String createTempShell(String opName,String packageId, VariableData variableData,String timestamp) throws Exception {
		VariableData myVariable=variableData;
		if (myVariable==null) {
			myVariable=getVariable(packageId);
		}
		String shellFileName=opName+"-"+timestamp+".sh";
		String shellFilePath=Constant.DIR_APP_PROJECT+packageId+"/package/"+shellFileName;
		File shellFile=new File(shellFilePath);
		List<String> shellLines=new ArrayList<String>();
		shellLines.add("#!/bin/sh");
		if (myVariable!=null && myVariable.getProperties()!=null) {
			Map<String, String> properties = myVariable.getProperties();
			for (Entry<String, String> entry : properties.entrySet()) {
				String line=entry.getKey()+" = "+ entry.getValue();
				shellLines.add(line);
			}
		}
		shellLines.add("source ./"+opName+".sh");
		FileUtils.writeLines(shellFile, shellLines);
		return shellFileName;
	}
	
	@Override
	public String uninstallPackage(String packageId, List<HostData> hostDatas,
			VariableData variableData) throws Exception {

		String timestamp=StringUtil.getTimeStamp();
		//write hosts file
		String hostsFileName=createHostsFile(packageId, hostDatas, variableData, timestamp);
		
		//shell already created when installed.
		String opName="uninstall";
		
		//exec command
		String logPath=Constant.DIR_APP_LOG+packageId+"-"+opName+"-"+timestamp+".log";;
		String cmd="ansible-playbook  -i "+hostsFileName+" "+opName+".yml --extra-vars \"package_id="+packageId+"\" -v ";
		ShellExecutor shellExecutor=new ShellExecutor(cmd, new File(logPath));
		shellExecutor.start();
		return logPath;
	
	}

	@Override
	public InstallLogData getInstallLog(String logId, int startIndex,
			int lineNum) {
		// TODO Auto-generated method stub
		return null;
	}

	private PackageData getPackageById(String packageId) throws Exception{
		List<PackageData> packages = getAllPackage(packageId);
		if (packages==null || packages.size()==0) {
			throw new Exception("can not find package: "+packageId);
		}
		return packages.get(0);
	}
	

}
