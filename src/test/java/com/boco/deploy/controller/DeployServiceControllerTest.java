package com.boco.deploy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.InstallLogData;
import com.boco.deploy.data.VariableData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeployServiceControllerTest {

	RestTemplate restTemplate = new RestTemplate();
	// String url="http://tpd4:8080/";
	String url="http://localhost:8080/";

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAllPackage() {
		List<?> list = restTemplate.getForObject(url + "package?exp={exp}", List.class, "");
		System.out.println(list);
		List<?> list2 = restTemplate.getForObject(url + "package?exp={exp}", List.class, "test");
		System.out.println(list2);
	}

	// @Test
	public void testGetVariable() {
		VariableData var = restTemplate.getForObject(url + "variable?packageId={packageId}", VariableData.class,
				"test_0.0.1");
		System.out.println(var.toString());
	}

	// @Test
	public void testSetVariable() {
		VariableData var = new VariableData();
		Map<String, String> properties = new HashMap<>();
		properties.put("k1", "v1");
		properties.put("test1", "test1");
		properties.put("test2", "test2");
		properties.put("testkey", "testvalue");
		var.setProperties(properties);
		SetVariableRequest request = new SetVariableRequest();
		request.setPackageId("k8s-v1.7.5");
		request.setVariableData(var);
		Boolean obj = restTemplate.postForObject(url + "variable", request, Boolean.class, "");
		System.out.println(obj);
	}

	// @Test
	public void testInstallPackage_master() {
		String packageId = "k8s-master_1.7.5.el7";

		HostData hostData2 = new HostData();
		hostData2.setIp("10.12.1.198");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");

		List<HostData> hostDatas = new ArrayList<HostData>();
		hostDatas.add(hostData2);

		VariableData variableData = new VariableData();
		InstallPackageRequest request = new InstallPackageRequest();
		request.setPackageId(packageId);
		request.setHostDatas(hostDatas);
		request.setVariableData(variableData);

		String result = restTemplate.postForObject(url + "install", request, String.class, new String[0]);
		System.out.println(result);
	}

	// @Test
	public void testInstallPackage_node() {
		String packageId = "k8s-node_1.7.5.el7";

		HostData hostData2 = new HostData();
		hostData2.setIp("10.12.1.197");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");

		List<HostData> hostDatas = new ArrayList<HostData>();
		hostDatas.add(hostData2);

		Map<String, String> properties = new HashMap<String, String>();
		properties.put("TOKEN", "7f1194.21c8935ef439b55e");
		properties.put("MASTER_IP_PORT", "10.12.1.198:6443");
		VariableData variableData = new VariableData();
		variableData.setProperties(properties);
		;
		InstallPackageRequest request = new InstallPackageRequest();
		request.setPackageId(packageId);
		request.setHostDatas(hostDatas);
		request.setVariableData(variableData);

		String result = restTemplate.postForObject(url + "install", request, String.class, new String[0]);
		System.out.println(result);
	}

	// @Test
	public void testUninstallPackage_node() {

		String packageId = "k8s-node_1.7.5.el7";

		HostData hostData2 = new HostData();
		hostData2.setIp("10.12.1.197");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");

		List<HostData> hostDatas = new ArrayList<HostData>();
		hostDatas.add(hostData2);

		Map<String, String> properties = new HashMap<String, String>();
		// properties.put("TOKEN", "7f1194.21c8935ef439b55e");
		// properties.put("MASTER_IP_PORT", "10.12.1.198:6443");
		VariableData variableData = new VariableData();
		variableData.setProperties(properties);
		;
		InstallPackageRequest request = new InstallPackageRequest();
		request.setPackageId(packageId);
		request.setHostDatas(hostDatas);
		request.setVariableData(variableData);

		String result = restTemplate.postForObject(url + "uninstall", request, String.class, new String[0]);
		System.out.println(result);

	}

	@Test
	public void testGetInstallLog() {
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("logId", "E:/tmp/logtest.txt");
		uriVariables.put("startIndex", -1);
		uriVariables.put("lineNum", 20);
		System.out.println(uriVariables);
		InstallLogData log = restTemplate.getForObject(
				url + "log?logId={logId}&startIndex={startIndex}&lineNum={lineNum}", InstallLogData.class,
				uriVariables);
		System.out.println(log.getData());

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("logId", "E:/tmp/loglogtest.txt");
		variables.put("startIndex", 0);
		variables.put("lineNum", 25);
		System.out.println(variables);
		InstallLogData log2 = restTemplate.getForObject(
				url + "log?logId={logId}&startIndex={startIndex}&lineNum={lineNum}", InstallLogData.class, variables);
		System.out.println(log2.getData());
	}

	private String toJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
