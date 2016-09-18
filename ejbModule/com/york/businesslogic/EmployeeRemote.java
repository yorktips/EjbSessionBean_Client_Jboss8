package com.york.businesslogic;

import javax.ejb.Remote;

import com.york.entity.*;

@Remote
public interface EmployeeRemote {
	public boolean addEmployee(Employee employee);
}