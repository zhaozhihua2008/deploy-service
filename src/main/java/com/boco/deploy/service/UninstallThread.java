package com.boco.deploy.service;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.InstanceData;
import com.boco.deploy.util.Constant;

public class UninstallThread extends AnsibleRunnable {
	private static final Logger logger = LoggerFactory.getLogger(UninstallThread.class);

	private InstanceData instance;
	private String logPath;
	private String projectPath;
	private String timestamp;
	
	public UninstallThread(String projectPath,InstanceData instance,String logPath,String timestamp) {
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
			
			// write hosts file
			String hostsFileName = createHostsFile(packageId,instanceName, hostData, timestamp);
			
			// exec command
			File dir = new File(projectPath + "/" + packageId);
			String cmd = "ansible-playbook  -i " + hostsFileName + " uninstall.yml -e instance_id=" + instanceId
				 + " -v ";
			File logFile = new File(logPath);
			FileUtils.forceMkdirParent(logFile);
			executeShell(dir, cmd, logFile);
		} catch (Exception e) {
			logger.error("execute install error:",e);
		}

	}
}
