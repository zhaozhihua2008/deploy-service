package com.boco.deploy.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.VariableData;
import com.boco.deploy.util.Constant;

public abstract class AnsibleRunnable implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(AnsibleRunnable.class);

	private String projectPath;
	
	public AnsibleRunnable(String projectPath) {
		super();
		this.projectPath = projectPath;
	}

	/**
	 * 根据hostData生成ansible hosts文件
	 */
	public String createHostsFile(String packageId,String instanceName, HostData hostData, String timestamp) throws Exception {
		// write hosts file
		String ipAndInstance = hostData.getIp()+Constant.UNDERLINE+instanceName;
		String hostsFileName = ipAndInstance + Constant.UNDERLINE + timestamp + ".hosts";
		String hostsFilePath = projectPath + "/" + packageId + "/" + hostsFileName;
		File hostsFile = new File(hostsFilePath);
		List<String> hostsLines = new ArrayList<String>();
		String line = hostData.getIp() + " ansible_ssh_user=" + hostData.getUserName() + " ansible_ssh_pass="
				+ hostData.getPassword();
		if (hostData.isSudo()) {
			line = line + " ansible_sudo_pass=" + hostData.getSudoPass() + " ansible_become=true";
		}
		hostsLines.add(line);
		FileUtils.writeLines(hostsFile, hostsLines);
		return hostsFileName;
	}

	/**
	 * 根据变量文件
	 */
	public String createVariableFile(String packageId,String instanceName, HostData hostData, VariableData variableData,
			String timestamp)
			throws Exception {
		// write hosts file
		String ipAndInstance = hostData.getIp()+Constant.UNDERLINE+instanceName;
		String fileName = ipAndInstance + Constant.UNDERLINE + timestamp + ".properties";
		String filePath = projectPath + "/" + packageId + "/" + fileName;
		File file = new File(filePath);
		List<String> lines = new ArrayList<String>();
		if (variableData!=null && variableData.getProperties()!=null) {
			Map<String, String> properties = variableData.getProperties();
			for (Entry<String, String> entry : properties.entrySet()) {
				String line=entry.getKey()+"="+entry.getValue();
				lines.add(line);
			}
		}
		FileUtils.writeLines(file, lines);
		return fileName;
	}
	
	public void executeShell(File dir,String cmd,File logFile){
		OutputStream output=null;
		InputStream input=null;
		try {
			output=new FileOutputStream(logFile);	
			logger.info("going to executeShell:"+cmd+",save log at:"+logFile.getName());
			Process proc = Runtime.getRuntime().exec(cmd,null,dir);
			proc.waitFor();
			input = proc.getInputStream();
			IOUtils.copy(input, output);
			InputStream errorStream = proc.getErrorStream();
			IOUtils.copy(errorStream, output);
		} catch (Exception e) {
			logger.error("executeShell error:",e);
		}finally{
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
}
