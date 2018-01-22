package com.boco.deploy.util;

public class Constant {

	final public static String DIR_APP_HOME = 
			System.getProperty("APP_HOME") != null? 
					System.getProperty("APP_HOME") + "/":"./";
	final public static String DIR_APP_CONF = DIR_APP_HOME + "conf/";
	final public static String DIR_APP_LOG = DIR_APP_HOME + "log/";
	final public static String PACKAGE_NAME = "package";
	
	final public static String UNDERLINE="_";
}
