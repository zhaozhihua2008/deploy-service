package com.boco.deploy.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${project.path}")
	private String projectPath;

	@Override
	public List<PackageData> getAllPackage(String exp) {
		System.out.println(projectPath);
		List<PackageData> list = new LinkedList<PackageData>();
		// 根据路径path读取目录中的文件夹
		String path = projectPath + "/";
		File file = new File(path);
		logger.info("在" + path + "下创建File的对象");
		if (file.exists()) {
			File[] files = file.listFiles();
			int len = files.length;
			logger.info("该路径下所有文件(夹)有:" + len + "个");
			// 分别创建names数组和versions数组
			String[] names = new String[len];
			String[] versions = new String[len];
			// 循环路径下所有文件夹
			for (int i = 0; i < len; i++) {
				if (files[i].isDirectory()) {
					// 将文件夹目录名截取
					String[] dirs = files[i].getName().split("_");
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
				logger.info("使用PackageData的对象循环接收属性:第" + (i + 1) + "个");
				if (names[i] != null && versions[i] != null) {
					// 无正则时全获取
					if (exp == null) {
						list.add(packageData);
						logger.info("无正则时默认获取全部包");
					} else {
						// 正则匹配
						Pattern pattern = Pattern.compile(exp);
						Matcher matcher = pattern.matcher(packageData.getId());
						logger.info("有正则时进行正则匹配");
						if (matcher.find()) { // 当前为模糊匹配
							logger.info("当前匹配方式为模糊匹配");
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
		logger.info("在" + path + "下创建File的对象");
		InputStream input = null;
		try {
			// 用property对象接收文件中的属性
			Properties property = new Properties();
			input = new BufferedInputStream(new FileInputStream(file));
			logger.info("从File的对象中生成输入流");
			property.load(input);
			logger.info("Properties的对象装载输入流");
			// 迭代property对象并分别取出属性的key和value放入map中
			Enumeration<?> en = property.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String value = property.getProperty(key);
				map.put(key, value);
			}
			logger.info("依次取出属性键值对");
			var.setProperties(map);
			logger.info("取出的属性键值对赋值给VariableData的对象并返回");
		} catch (IOException e) {
			logger.error("生成输入流遇到IO异常,请检查文件路径" + path);
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("关闭输入流遇到IO异常");
					e.printStackTrace();
				}
			}
		}
		return var;
	}

	@Override
	public boolean setVariable(String packageId, VariableData variableData) {
		boolean result = false;
		// 把参数variableData中的属性放入map集合
		Map<String, String> map = variableData.getProperties();
		logger.info("把参数variableData中的属性放入map集合");
		// 根据path路径创建file对象
		String path = projectPath + "/" + packageId + "/" + Constant.PACKAGE_NAME + "/variable.properties";
		File file = new File(path);
		logger.info("在" + path + "下创建File的对象");
		OutputStream output = null;
		Properties property = new Properties();
		try {
			output = new FileOutputStream(file);
			logger.info("在该路径创建输出流OutputStream的对象");
			// 遍历读取map集合中的属性并用property对象循环接收
			Iterator<Entry<String, String>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
				String key = entry.getKey();
				String value = entry.getValue();
				property.setProperty(key, value);
			}
			logger.info("遍历读取map集合中的属性并用Properties的对象循环接收");
			// 把property对象写入输出流并返回值置为true
			property.store(output, "Updated properties");
			result = true;
			logger.info("接收成功,把Properties的对象写入输出流并将返回值置为true");
		} catch (FileNotFoundException e) {
			logger.error("找不到文件,请检查文件路径" + path);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("写入Properties的对象失败,遇到IO异常");
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					logger.error("关闭输出流遇到IO异常");
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public String installPackage(String packageId, List<HostData> hostDatas, VariableData variableData)
			throws Exception {
		String timestamp = StringUtil.getTimeStamp();
		// write hosts file
		String hostsFileName = createHostsFile(packageId, hostDatas, variableData, timestamp);

		// create temp shell
		String opName = "install";
		createTempShell(opName, packageId, variableData, timestamp);
		createTempShell("uninstall", packageId, variableData, timestamp);

		// exec command
		String logPath = projectPath + "/log/" + packageId + "-" + opName + "-" + timestamp + ".log";
		File dir = new File(projectPath + "/" + packageId);
		String cmd = "ansible-playbook  -i " + hostsFileName + " " + opName + ".yml -e package_id=" + packageId
				+ " -e timestamp=" + timestamp + " -v ";
		File logFile = new File(logPath);
		FileUtils.forceMkdirParent(logFile);
		ShellExecutor shellExecutor = new ShellExecutor(dir, cmd, logFile);
		shellExecutor.start();
		return logPath;
	}

	/**
	 * 根据hostDatas生成ansible hosts文件
	 */
	private String createHostsFile(String packageId, List<HostData> hostDatas, VariableData variableData,
			String timestamp) throws Exception {
		// write hosts file
		String firstHost = hostDatas.get(0).getIp();
		String hostsFileName = firstHost + "-" + timestamp + ".hosts";
		String hostsFilePath = projectPath + "/" + packageId + "/" + hostsFileName;
		File hostsFile = new File(hostsFilePath);
		List<String> hostsLines = new ArrayList<String>();
		for (HostData hostData : hostDatas) {
			String line = hostData.getIp() + " ansible_ssh_user=" + hostData.getUserName() + " ansible_ssh_pass="
					+ hostData.getPassword();
			if (hostData.isSudo()) {
				line = line + " ansible_sudo_pass=" + hostData.getSudoPass() + " ansible_become=true";
			}
			hostsLines.add(line);
		}
		FileUtils.writeLines(hostsFile, hostsLines);
		return hostsFileName;
	}

	/**
	 * 根据变量创建临时脚本
	 */
	private String createTempShell(String opName, String packageId, VariableData variableData, String timestamp)
			throws Exception {
		VariableData myVariable = variableData;
		if (myVariable == null) {
			myVariable = getVariable(packageId);
		}
		String shellFileName = opName + "-" + timestamp + ".sh";
		String shellFilePath = projectPath + "/" + packageId + "/package/" + shellFileName;
		File shellFile = new File(shellFilePath);
		List<String> shellLines = new ArrayList<String>();
		shellLines.add("#!/bin/sh");
		if (myVariable != null && myVariable.getProperties() != null) {
			Map<String, String> properties = myVariable.getProperties();
			for (Entry<String, String> entry : properties.entrySet()) {
				String line = entry.getKey() + "=" + entry.getValue();
				shellLines.add(line);
			}
		}
		shellLines.add("source ./" + opName + ".sh");
		FileUtils.writeLines(shellFile, shellLines);
		return shellFileName;
	}

	@Override
	public String uninstallPackage(String packageId, List<HostData> hostDatas, VariableData variableData)
			throws Exception {

		String timestamp = StringUtil.getTimeStamp();
		// write hosts file
		String hostsFileName = createHostsFile(packageId, hostDatas, variableData, timestamp);

		// shell already created when installed.
		String opName = "uninstall";

		// exec command
		String logPath = projectPath + "/log/" + packageId + "-" + opName + "-" + timestamp + ".log";
		File dir = new File(projectPath + "/" + packageId);
		String cmd = "ansible-playbook  -i " + hostsFileName + " " + opName + ".yml -e package_id=" + packageId
				+ " -v ";
		ShellExecutor shellExecutor = new ShellExecutor(dir, cmd, new File(logPath));
		shellExecutor.start();
		return logPath;

	}

	@Override
	public InstallLogData getInstallLog(String logId, int startIndex, int lineNum) {
		InstallLogData log = new InstallLogData();
		log.setLogId(logId);
		// 根据path路径创建file对象
		String path = Constant.DIR_APP_LOG + logId;
		File file = new File(path);
		logger.info("在" + path + "下创建File的对象");
		BufferedReader reader = null;
		logger.info("创建BufferedReader对象");
		StringBuilder builder = new StringBuilder();
		// 读入文件内容最后放入list集合
		List<String> list = new ArrayList<String>();
		logger.info("创建StringBuilder字符串对象和List集合对象");
		int index = 0;// 文件行数
		boolean eof = false;
		try {
			reader = new BufferedReader(new FileReader(file));
			logger.info("读取File对象");
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				list.add("当前第" + (index + 1) + "行: " + tempString);
				index++;
			}
			logger.info("按行读取日志文件,共有" + index + "行");
			log.setEndIndex(index);
			if (index == 0) {
				logger.debug("当前日志为空");
				eof = true;
			} else if (startIndex == -1) { // 倒序读取日志
				logger.info("startIndex为:" + startIndex + ",执行倒序读取日志");
				// 获取行数小于0行或者超过能获取的最大行数时，只能获取当前最大行数的日志
				if (lineNum > index || lineNum < 0) {
					logger.info("获取行数小于0行或者超过能获取的最大行数时，只能获取当前最大行数的日志");
					System.out.println("当前最多只能获取" + index + "行");
					logger.debug("当前最多只能获取" + index + "行");
					lineNum = index;
					eof = true;
				}
				for (int i = index - 1; i >= index - lineNum; i--) {
					builder.append(list.get(i));
				}
				logger.info("循环倒序读取日志");
			} else if (startIndex >= 0 && startIndex <= index) {
				// 起始为0时从第一行开始
				if (startIndex == 0) {
					startIndex++;
					logger.info("起始为0时从第一行开始");
				}
				// 获取行数小于0行或者超过能获取的最大行数时，只能获取当前最大行数的日志
				if (lineNum >= index - startIndex + 1 || lineNum < 0) {
					logger.info("获取行数小于0行或者超过能获取的最大行数时，只能获取当前最大行数的日志");
					System.out.println("当前最多只能获取" + (index - startIndex + 1) + "行");
					logger.debug("当前最多只能获取" + (index - startIndex + 1) + "行");
					lineNum = index - startIndex + 1;
					eof = true;
				}
				for (int i = startIndex - 1; i < startIndex + lineNum - 1; i++) {
					if (i < index) {
						builder.append(list.get(i));
					}
				}
				logger.info("循环读取日志");
				// 起始行数超过最大行时，只能获取从当前最大行数开始的日志
			} else if (startIndex > index) {
				System.out.println("当前只有" + index + "行");
				startIndex = index;
				eof = true;
				logger.debug("起始行数:" + startIndex + "超过最大行:" + (index + 1) + "时，只能获取从当前最大行数开始的日志");
			} else {
				logger.error("startIndex不能为" + startIndex + "!请输入正确的startIndex值");
			}
			log.setData(builder.toString());
			log.setEof(eof);
			logger.info("保存读取后的日志文件到InstallLogData对象的Data并返回");
		} catch (FileNotFoundException e) {
			logger.error("找不到日志文件,请检查路径:" + path);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("读取日志文件发生IO异常");
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					logger.error("关闭BufferedReader对象发生IO异常");
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
