package com.boco.deploy.service;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShellExecutorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRun() throws InterruptedException {
		File file=new File("c:/tmp/a.log");
		ShellExecutor executor=new ShellExecutor(new File("c:/tmp"),"cmd /c dir",file);
		executor.start();
		executor.join();
	}

}
