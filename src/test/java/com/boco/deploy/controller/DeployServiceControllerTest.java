package com.boco.deploy.controller;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.boco.deploy.data.VariableData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeployServiceControllerTest {

	RestTemplate restTemplate = new RestTemplate();
	String url="http://localhost:8080/";
	

	
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
	public void testInstallPackage() {
		fail("Not yet implemented");
	}

	@Test
	public void testUninstallPackage() {
		fail("Not yet implemented");
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
