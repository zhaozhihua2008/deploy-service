package com.boco.deploy.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.boco.deploy.data.HostData;
import com.boco.deploy.data.VariableData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeployServiceControllerTest {

	RestTemplate restTemplate = new RestTemplate();
	String url="http://tpd4:8080/";
	

	
	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAllPackage() {
		List list = restTemplate.getForObject(url+"package?exp={exp}", List.class, "");
		System.out.println(list);
	}

	@Test
	public void testGetVariable() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetVariable() {
		Map<String, String> properties=new HashMap<String, String>();
		properties.put("k1", "v1");
		VariableData data=new VariableData();
		data.setProperties(properties);
		SetVariableRequest request=new SetVariableRequest();
		request.setPackageId("k8s-v1.7.5");
		request.setVariableData(data);
		Boolean obj = restTemplate.postForObject(url+"variable", data, Boolean.class, "");
		System.out.println(obj);
	}

	@Test
	public void testInstallPackage_master() {
		String packageId="k8s-master_1.7.5.el7";
		
		HostData hostData2=new HostData();
		hostData2.setIp("10.12.1.196");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");
		
		List<HostData> hostDatas=new ArrayList<HostData>();
		hostDatas.add(hostData2);
		
		VariableData variableData=new VariableData();
		InstallPackageRequest request=new InstallPackageRequest();
		request.setPackageId(packageId);
		request.setHostDatas(hostDatas);
		request.setVariableData(variableData);
		
		String result = restTemplate.postForObject(url+"install", request, String.class, new String[0]);
		System.out.println(result);
	}

	@Test
	public void testInstallPackage_node() {
		String packageId="k8s-node_1.7.5.el7";
		
		HostData hostData2=new HostData();
		hostData2.setIp("10.12.1.197");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");
		
		List<HostData> hostDatas=new ArrayList<HostData>();
		hostDatas.add(hostData2);
		
		Map<String, String> properties=new HashMap<String, String>();
		properties.put("TOKEN", "7f1194.21c8935ef439b55e");
		properties.put("MASTER_IP_PORT", "10.12.1.198:6443");
		VariableData variableData=new VariableData();
		variableData.setProperties(properties);;
		InstallPackageRequest request=new InstallPackageRequest();
		request.setPackageId(packageId);
		request.setHostDatas(hostDatas);
		request.setVariableData(variableData);
		
		String result = restTemplate.postForObject(url+"install", request, String.class, new String[0]);
		System.out.println(result);
	}
	
	@Test
	public void testUninstallPackage_master() {

		String packageId="k8s-master_1.7.5.el7";
		
		HostData hostData2=new HostData();
		hostData2.setIp("10.12.1.196");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");
		
		List<HostData> hostDatas=new ArrayList<HostData>();
		hostDatas.add(hostData2);
		
		Map<String, String> properties=new HashMap<String, String>();
//		properties.put("TOKEN", "7f1194.21c8935ef439b55e");
//		properties.put("MASTER_IP_PORT", "10.12.1.198:6443");
		VariableData variableData=new VariableData();
		variableData.setProperties(properties);;
		InstallPackageRequest request=new InstallPackageRequest();
		request.setPackageId(packageId);
		request.setHostDatas(hostDatas);
		request.setVariableData(variableData);
		
		String result = restTemplate.postForObject(url+"uninstall", request, String.class, new String[0]);
		System.out.println(result);
	
	}
	
	@Test
	public void testUninstallPackage_node() {

		String packageId="k8s-node_1.7.5.el7";
		
		HostData hostData2=new HostData();
		hostData2.setIp("10.12.1.197");
		hostData2.setUserName("root");
		hostData2.setPassword("yiyangboco");
		
		List<HostData> hostDatas=new ArrayList<HostData>();
		hostDatas.add(hostData2);
		
		Map<String, String> properties=new HashMap<String, String>();
//		properties.put("TOKEN", "7f1194.21c8935ef439b55e");
//		properties.put("MASTER_IP_PORT", "10.12.1.198:6443");
		VariableData variableData=new VariableData();
		variableData.setProperties(properties);;
		InstallPackageRequest request=new InstallPackageRequest();
		request.setPackageId(packageId);
		request.setHostDatas(hostDatas);
		request.setVariableData(variableData);
		
		String result = restTemplate.postForObject(url+"uninstall", request, String.class, new String[0]);
		System.out.println(result);
	
	}

	@Test
	public void testGetInstallLog() {
		fail("Not yet implemented");
	}

	
	private String toJson(Object obj){
		ObjectMapper mapper=new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
