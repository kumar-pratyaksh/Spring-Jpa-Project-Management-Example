package com.proptiger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proptiger.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	public List<Employee> findByNameContainingIgnoreCase(String name);
	// public List<Project> findAllProjects();
}
