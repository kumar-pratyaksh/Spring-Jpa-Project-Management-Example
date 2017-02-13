package com.proptiger.services;

import java.util.List;

import com.proptiger.model.Employee;

public interface EmployeeService {

	public void addEmployee(Employee e);

	public void updateEmployee(Employee e, Long id);

	public List<Employee> findAll();

	public Employee findOne(Long id);

	public List<Employee> findByName(String name);

	public void delete(Long id);

	public void updateScore(Long id);
}
