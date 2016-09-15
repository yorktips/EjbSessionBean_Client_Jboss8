package com.york.clientutility;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
 
public class ClientUtility {

	private static Context initialContext;
	private static String filename="jboss-ejb-client.properties";
	
	public static Context getInitialContext() throws NamingException {
		if (initialContext == null) {
			InputStream input = null;
			Properties jndiProperties = new Properties();
			try {
				input = ClientUtility.class.getClassLoader().getResourceAsStream(filename);
				//input = new FileInputStream(filename);
				jndiProperties.load(input);
				initialContext = new InitialContext(jndiProperties);
				return initialContext;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return initialContext;
	}
}