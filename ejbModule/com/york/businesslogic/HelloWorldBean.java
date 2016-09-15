package com.york.businesslogic;

import javax.ejb.Stateless;

import com.york.businesslogic.HelloWorld;

import javax.ejb.Stateless;

/**
 * Session Bean implementation class HelloWorldBean
 */
@Stateless
public class HelloWorldBean implements HelloWorld, HelloWorldBeanLocal {

    /**
     * Default constructor. 
     */
    public HelloWorldBean() {
        // TODO Auto-generated constructor stub
    }

    public String sayHello() {
        return "Hello World !!!";
    }
    
}
