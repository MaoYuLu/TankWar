package com.maoyulu;

import java.io.IOException;
import java.util.Properties;

public class ProperMgr {

	static Properties properties =  new Properties();
	
	static {
		try {
			properties.load(ProperMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ProperMgr() {
		
	}
	
	public static String getProperty(String key) {		
		return properties.getProperty(key);	
	}
}
