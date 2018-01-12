package com.boco.deploy.service;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.VariableData;

public class DeployServiceImplTest {
	private static final Logger logger = LoggerFactory.getLogger(DeployServiceImplTest.class);

	DeployServiceImpl impl=null;
	@Before
	public void setUp() throws Exception {
		System.setProperty("APP_HOME", "c:/tmp");
		impl=new DeployServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInstallPackage() throws Exception {
		String packageId="k8s-master_1.7.5.el7";
		HostData hostData1=new HostData();
		hostData1.setIp("tpd1");
		hostData1.setUserName("xdpp");
		hostData1.setPassword("xdpp");
		hostData1.setSudo(true);
		hostData1.setSudoPass("xdpp");
		
		HostData hostData2=new HostData();
		hostData2.setIp("tpd2");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");
		
		List<HostData> hostDatas=new ArrayList<HostData>();
		hostDatas.add(hostData1);
		hostDatas.add(hostData2);
		
		Map<String, String> properties=new HashMap<String, String>();
		properties.put("k1", "v1");
		VariableData variableData=new VariableData();
		variableData.setProperties(properties);
		impl.installPackage(packageId, hostDatas, variableData);
	}

	@Test
	public void testUninstallPackage() throws Exception {

		String packageId="k8s-master_1.7.5.el7";
		HostData hostData1=new HostData();
		hostData1.setIp("tpd1");
		hostData1.setUserName("xdpp");
		hostData1.setPassword("xdpp");
		hostData1.setSudo(true);
		hostData1.setSudoPass("xdpp");
		
		HostData hostData2=new HostData();
		hostData2.setIp("tpd2");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");
		
		List<HostData> hostDatas=new ArrayList<HostData>();
		hostDatas.add(hostData1);
		hostDatas.add(hostData2);
		
		Map<String, String> properties=new HashMap<String, String>();
		properties.put("k1", "v1");
		VariableData variableData=new VariableData();
		variableData.setProperties(properties);
		impl.uninstallPackage(packageId, hostDatas, variableData);
	
	}

}
