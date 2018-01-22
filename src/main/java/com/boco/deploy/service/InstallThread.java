package com.boco.deploy.service;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.InstanceData;
import com.boco.deploy.data.VariableData;
import com.boco.deploy.util.Constant;

public class InstallThread extends AnsibleRunnable {
	private static final Logger logger = LoggerFactory.getLogger(InstallThread.class);

	private String projectPath;
	private InstanceData instance;
	private String logPath;
	private String timestamp;
	
	public InstallThread(String projectPath,InstanceData instance,String logPath,String timestamp) {
		super(projectPath);
		this.projectPath = projectPath;
		this.instance = instance;
		this.logPath = logPath;
		this.timestamp = timestamp;
	}

	@Override
	public void run() {
		try {
			String instanceName = instance.getInstanceName();
			String packageId= instance.getPackageData().getId();
			String instanceId = packageId+Constant.UNDERLINE+instanceName;
			HostData hostData = instance.getHostData();
			VariableData variableData = instance.getVariableData();
			
			// write hosts file
			String hostsFileName = createHostsFile(packageId,instanceName, hostData, timestamp);
			logger.info("create hosts file:"+hostsFileName);
			// create varibale file
			String variableFileName = createVariableFile(packageId, instanceName, hostData, variableData, timestamp);
			logger.info("create varibale file:"+variableFileName);

			// exec command
			File dir = new File(projectPath + "/" + packageId);
			String cmd = "ansible-playbook  -i " + hostsFileName + " install.yml -e instance_id=" + instanceId
					+ " -e var_file_name=" + variableFileName + " -v ";
			File logFile = new File(logPath);
			FileUtils.forceMkdirParent(logFile);
			executeShell(dir, cmd, logFile);
		} catch (Exception e) {
			logger.error("execute install error:",e);
		}

	}
}
