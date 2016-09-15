package com.york.businesslogic;

import javax.ejb.Local;

@Local
public interface HelloWorldBeanLocal {
	public String sayHello();
}
