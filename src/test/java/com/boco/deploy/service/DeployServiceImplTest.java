package com.boco.deploy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.InstallLogData;
import com.boco.deploy.data.PackageData;
import com.boco.deploy.data.VariableData;

public class DeployServiceImplTest {
	private static final Logger logger = LoggerFactory.getLogger(DeployServiceImplTest.class);

	DeployServiceImpl impl = null;

	@Before
	public void setUp() throws Exception {
		// System.setProperty("APP_HOME", "c:/tmp");
		System.setProperty("APP_HOME", "E:/tmp");
		impl = new DeployServiceImpl();
		impl.setProjectPath("E:/tmp/project");
		logger.info("setProjectPath(\"E:/tmp/project\")");
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	public void testInstallPackage() throws Exception {
		String packageId = "k8s-master_1.7.5.el7";
		HostData hostData1 = new HostData();
		hostData1.setIp("tpd1");
		hostData1.setUserName("xdpp");
		hostData1.setPassword("xdpp");
		hostData1.setSudo(true);
		hostData1.setSudoPass("xdpp");

		HostData hostData2 = new HostData();
		hostData2.setIp("tpd2");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");

		List<HostData> hostDatas = new ArrayList<HostData>();
		hostDatas.add(hostData1);
		hostDatas.add(hostData2);

		Map<String, String> properties = new HashMap<String, String>();
		properties.put("k1", "v1");
		VariableData variableData = new VariableData();
		variableData.setProperties(properties);
		impl.installPackage(packageId, hostDatas, variableData);
	}

	// @Test
	public void testUninstallPackage() throws Exception {

		String packageId = "k8s-master_1.7.5.el7";
		HostData hostData1 = new HostData();
		hostData1.setIp("tpd1");
		hostData1.setUserName("xdpp");
		hostData1.setPassword("xdpp");
		hostData1.setSudo(true);
		hostData1.setSudoPass("xdpp");

		HostData hostData2 = new HostData();
		hostData2.setIp("tpd2");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");

		List<HostData> hostDatas = new ArrayList<HostData>();
		hostDatas.add(hostData1);
		hostDatas.add(hostData2);

		Map<String, String> properties = new HashMap<String, String>();
		properties.put("k1", "v1");
		VariableData variableData = new VariableData();
		variableData.setProperties(properties);
		impl.uninstallPackage(packageId, hostDatas, variableData);

	}

	// @Test
	public void testGetAllPackage() {
		List<PackageData> allPackage = impl.getAllPackage(null);
		for (int i = 0; i < allPackage.size(); i++) {
			System.out.println("输入正则为null时默认匹配全部: 第" + (i + 1) + "个");
			System.out.println("包名:" + allPackage.get(i).getName());
			System.out.println("版本号:" + allPackage.get(i).getVersion());
			System.out.println("包id:" + allPackage.get(i).getId());
		}
		System.out.println();
		List<PackageData> packages1 = impl.getAllPackage("");
		for (int i = 0; i < packages1.size(); i++) {
			System.out.println("输入正则为空字符串时默认匹配全部: 第" + (i + 1) + "个");
			System.out.println("包名:" + allPackage.get(i).getName());
			System.out.println("版本号:" + allPackage.get(i).getVersion());
			System.out.println("包id:" + allPackage.get(i).getId());
		}
		System.out.println();
		List<PackageData> packages2 = impl.getAllPackage("test");
		for (int i = 0; i < packages2.size(); i++) {
			System.out.println("输入正则为test时的匹配: 第" + (i + 1) + "个");
			System.out.println("包名:" + allPackage.get(i).getName());
			System.out.println("版本号:" + allPackage.get(i).getVersion());
			System.out.println("包id:" + allPackage.get(i).getId());
		}
		System.out.println();
		List<PackageData> packages3 = impl.getAllPackage("1");
		for (int i = 0; i < packages3.size(); i++) {
			System.out.println("输入正则为1时的匹配: 第" + (i + 1) + "个");
			System.out.println("包名:" + allPackage.get(i).getName());
			System.out.println("版本号:" + allPackage.get(i).getVersion());
			System.out.println("包id:" + allPackage.get(i).getId());
		}
	}

	// @Test
	public void testGetVariable() {
		VariableData var1 = impl.getVariable("test_0.0.1");
		System.out.println("packageId为 test_0.0.1时获取的属性:" + var1.toString());
		VariableData var2 = impl.getVariable(null);
		System.out.println("packageId为 null时获取的属性:" + var2.toString());
	}

	// @Test
	public void testSetVariable() {
		VariableData var = new VariableData();
		Map<String, String> properties = new HashMap<>();
		properties.put("test1", "test1");
		properties.put("test2", "test2");
		properties.put("testkey", "testvalue");
		var.setProperties(properties);
		if (impl.setVariable("123_test", var)) {
			System.out.println(var.toString());
		}
	}

	@Test
	public void testGetInstallLog() {
		String id1 = "logtest.txt";
		int start1 = 1;
		int num1 = 7;
		InstallLogData log1 = impl.getInstallLog(id1, start1, num1);
		System.out.println("参数列表:\n当前日志文件id是:" + id1 + ",开始行数startIndex是" + start1 + ",读取行数:" + num1 + "\n文件属性:\n总行数:"
				+ log1.getEndIndex() + ",日志内容:" + log1.getData() + "\n日志文件是否读取结束:" + log1.isEof() + "\n");

		String id2 = "logtest.txt";
		int start2 = 0;
		int num2 = 27;
		InstallLogData log2 = impl.getInstallLog(id2, start2, num2);
		System.out.println("参数列表:\n当前日志文件id是:" + id2 + ",开始行数startIndex是" + start2 + ",读取行数:" + num2 + "\n文件属性:\n总行数:"
				+ log2.getEndIndex() + ",日志内容:" + log2.getData() + "\n日志文件是否读取结束:" + log2.isEof() + "\n");

		String id3 = "logtest.txt";
		int start3 = -1;
		int num3 = 17;
		InstallLogData log3 = impl.getInstallLog(id3, start3, num3);
		System.out.println("参数列表:\n当前日志文件id是:" + id3 + ",开始行数startIndex是" + start3 + ",读取行数:" + num3 + "\n文件属性:\n总行数:"
				+ log3.getEndIndex() + ",日志内容:" + log3.getData() + "\n日志文件是否读取结束:" + log3.isEof() + "\n");

		String id4 = "logtest.txt";
		int start4 = -1;
		int num4 = 37;
		InstallLogData log4 = impl.getInstallLog(id4, start4, num4);
		System.out.println("参数列表:\n当前日志文件id是:" + id4 + ",开始行数startIndex是" + start4 + ",读取行数:" + num4 + "\n文件属性:\n总行数:"
				+ log4.getEndIndex() + ",日志内容:" + log4.getData() + "\n日志文件是否读取结束:" + log4.isEof() + "\n");

		String id5 = "logtest.txt";
		int start5 = -20;
		int num5 = 37;
		InstallLogData log5 = impl.getInstallLog(id5, start5, num5);
		System.out.println("参数列表:\n当前日志文件id是:" + id5 + ",开始行数startIndex是" + start5 + ",读取行数:" + num5 + "\n文件属性:\n总行数:"
				+ log5.getEndIndex() + ",日志内容:" + log5.getData() + "\n日志文件是否读取结束:" + log5.isEof() + "\n");

		String id6 = "none.txt";
		int start6 = 25;
		int num6 = 37;
		InstallLogData log6 = impl.getInstallLog(id6, start6, num6);
		System.out.println("参数列表:\n当前日志文件id是:" + id6 + ",开始行数startIndex是" + start6 + ",读取行数:" + num6 + "\n文件属性:\n总行数:"
				+ log6.getEndIndex() + ",日志内容:" + log6.getData() + "\n日志文件是否读取结束:" + log6.isEof() + "\n");

	}
}
