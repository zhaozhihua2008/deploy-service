package com.boco.deploy.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.boco.deploy.data.InstallLogData;
import com.boco.deploy.data.InstanceData;
import com.boco.deploy.data.PackageData;
import com.boco.deploy.data.VariableData;
import com.boco.deploy.util.Constant;
import com.boco.deploy.util.ExecutorServiceManager;
import com.boco.deploy.util.StringUtil;

@Service
public class DeployServiceImpl implements DeployService {
	private static Logger logger = LoggerFactory.getLogger(DeployServiceImpl.class);

	@Value("${project.path}")
	private String projectPath;

	@Override
	public List<PackageData> getAllPackage(String exp) {
		List<PackageData> list = new LinkedList<PackageData>();
		// 根据路径path读取目录中的文件夹
		String path = projectPath + "/";
		File file = new File(path);
		logger.info("Create a File object in this path:" + path);
		if (file.exists()) {
			File[] files = file.listFiles();
			int len = files.length;
			logger.info("All files (folders) in this path have a total of:" + len );
			// 分别创建names数组和versions数组
			String[] names = new String[len];
			String[] versions = new String[len];
			// 循环路径下所有文件夹
			for (int i = 0; i < len; i++) {
				if (files[i].isDirectory()) {
					// 将文件夹目录名截取
					String[] dirs = files[i].getName().split(Constant.UNDERLINE);
					if (dirs.length == 2) {
						for (int j = 0; j < dirs.length; j++) {
							names[i] = dirs[0];
							versions[i] = dirs[1];
						}
					}
				}
				// 循环创建PackageData对象来接收属性值
				PackageData packageData = new PackageData();
				packageData.setName(names[i]);
				packageData.setVersion(versions[i]);
				logger.info("Using PackageData object receives properties cyclically:This is NO." + (i + 1));
				if (names[i] != null && versions[i] != null) {
					// 无正则时全获取
					if (exp == null) {
						list.add(packageData);
						logger.info("No regular express to get all the packages by default");
					} else {
						// 正则匹配
						Pattern pattern = Pattern.compile(exp);
						Matcher matcher = pattern.matcher(packageData.getId());
						logger.info("There are regular express matching:");
						if (matcher.find()) { // 当前为模糊匹配
							logger.info("The current matching method is fuzzy matching by matcher.find()");
							list.add(packageData);
						}
					}
				}
			}
		}
		return list;
	}

	@Override
	public VariableData getVariable(String packageId) {
		VariableData var = new VariableData();
		Map<String, String> map = new HashMap<String, String>();
		// 根据path路径创建file对象
		String path = projectPath + "/" + packageId + "/" + Constant.PACKAGE_NAME + "/variable.properties";
		File file = new File(path);
		logger.info("Create a File object in this path:" + path);
		InputStream input = null;
		try {
			// 用property对象接收文件中的属性
			Properties property = new Properties();
			input = new BufferedInputStream(new FileInputStream(file));
			logger.info("Create an input stream from the File's object");
			property.load(input);
			logger.info("Properties object loads the input stream");
			// 迭代property对象并分别取出属性的key和value放入map中
			Enumeration<?> en = property.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String value = property.getProperty(key);
				map.put(key, value);
			}
			logger.info("Get the properties' key-value pairs in turn");
			var.setProperties(map);
			logger.info("The getted properties' key-value pairs are assigned to the VariableData object and returned");
		} catch (IOException e) {
			logger.error("Creating the input stream failed by an IO exception, please check the file path:" + path);
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("Closing the input stream failed by an IO exception");
					e.printStackTrace();
				}
			}
		}
		return var;
	}

	@Override
	public List<String> installPackage(List<InstanceData> instanceDatas)throws Exception {
		List<String> logPaths=new ArrayList<String>();
		String timestamp = StringUtil.getTimeStamp();
		for (InstanceData instance : instanceDatas) {
			String instanceName = instance.getInstanceName();
			String packageId= instance.getPackageData().getId();
			String instanceId = packageId+Constant.UNDERLINE+instanceName;
			String logPath = projectPath + "/log/" + instanceId + "_install_" + timestamp + ".log";
			logPaths.add(logPath);
			logger.info("going to install instance: "+instanceId);
			InstallThread installThread=new InstallThread(projectPath, instance, logPath, timestamp);
			ExecutorServiceManager.getInstance().getExecutor().execute(installThread);
		}
		
		return logPaths;
	}

	@Override
	public List<String> uninstallPackage(List<InstanceData> instanceDatas)throws Exception {
		List<String> logPaths=new ArrayList<String>();
		String timestamp = StringUtil.getTimeStamp();
		for (InstanceData instance : instanceDatas) {
			String instanceName = instance.getInstanceName();
			String packageId= instance.getPackageData().getId();
			String instanceId = packageId+Constant.UNDERLINE+instanceName;
			String logPath = projectPath + "/log/" + instanceId + "_uninstall_" + timestamp + ".log";
			logPaths.add(logPath);
			logger.info("going to uninstall instance: "+instanceId);
			UninstallThread uninstallThread=new UninstallThread(projectPath, instance, logPath, timestamp);
			ExecutorServiceManager.getInstance().getExecutor().execute(uninstallThread);
		}
		return logPaths;
	
	}

	@Override
	public InstallLogData getInstallLog(String logId, int startIndex, int lineNum) {
		InstallLogData log = new InstallLogData();
		log.setLogId(logId);
		// 根据path路径创建file对象
		String path = projectPath +"/" + logId;
		File file = new File(path);
		logger.info("Create a File object in this path:" + path);
		BufferedReader reader = null;
		logger.info("Create a BufferedReader object");
		StringBuilder builder = new StringBuilder();
		// 读入文件内容最后放入list集合
		List<String> list = new ArrayList<String>();
		logger.info("Create a StringBuilder string object and List collection object");
		int index = 0;// 文件行数
		boolean eof = false;
		try {
			reader = new BufferedReader(new FileReader(file));
			logger.info("Read File object");
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				// list.add("This is No." + (index + 1) + " line:" + tempString);
				list.add(tempString);
				index++;
			}
			logger.info("Reading log files by line,total number of lines is:" + index);
			log.setEndIndex(index);
			if (index == 0) {
				logger.debug("The current log file is empty!");
				eof = true;
			} else if (startIndex == -1) { // 倒序读取日志
				logger.info("Executing reverse read log file.Start index is:" + startIndex);
				// 获取行数小于0行或者超过能获取的最大行数时，只能获取当前最大行数的日志
				if (lineNum > index || lineNum < 0) {
					logger.info("Only can get the current maximum number of lines in this log if (lineNum > index || lineNum < 0)");
					logger.debug("Parameter lineNum overflow!Currently total number of lines is only:" + index );
					lineNum = index;
					eof = true;
				}
				for (int i = index - 1; i >= index - lineNum; i--) {
					builder.append(list.get(i));
				}
				logger.info("Cyclically read the log in reverse order");
			} else if (startIndex >= 0 && startIndex <= index) {
				// 起始为0时从第一行开始
				if (startIndex == 0) {
					startIndex++;
					logger.info("Start at first line when starting from 0");
				}
				// 获取行数小于0行或者超过能获取的最大行数时，只能获取当前最大行数的日志
				if (lineNum >= index - startIndex + 1 || lineNum < 0) {
					logger.info("Only can get the current maximum number of lines in this log if (lineNum >= index - startIndex + 1 || lineNum < 0)");
					logger.debug("Parameter lineNum overflow!Currently total number of lines is only:" + (index - startIndex + 1));
					lineNum = index - startIndex + 1;
					eof = true;
				}
				for (int i = startIndex - 1; i < startIndex + lineNum - 1; i++) {
					if (i < index) {
						builder.append(list.get(i));
					}
				}
				logger.info("Read the log cyclically");
				// 起始行数超过最大行时，只能获取从当前最大行数开始的日志
			} else if (startIndex > index) {
				startIndex = index;
				eof = true;
				logger.debug("Parameter startIndex overflow!Currently total number of lines is only:" + index );
			} else {
				logger.error("Parameter startIndex:" + startIndex + "is illegal!Please enter correct startIndex!");
			}
			log.setData(builder.toString());
			log.setEof(eof);
			logger.info("Save the read log file to InstallLogData object's data and return");
		} catch (FileNotFoundException e) {
			logger.error("Can not find the log file, please check the file path:" + path);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Reading log file failed by an IO exception");
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					logger.error("Closing BufferedReader failed by an IO exception");
				}
			}
		}
		return log;
	}

	@SuppressWarnings("unused")
	private PackageData getPackageById(String packageId) throws Exception {
		List<PackageData> packages = getAllPackage(packageId);
		if (packages == null || packages.size() == 0) {
			throw new Exception("can not find package: " + packageId);
		}
		return packages.get(0);
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

}
