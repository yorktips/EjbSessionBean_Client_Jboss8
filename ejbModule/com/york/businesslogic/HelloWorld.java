package com.york.businesslogic;

import javax.ejb.Remote;

@Remote
public interface HelloWorld {
	public String sayHello();
}
