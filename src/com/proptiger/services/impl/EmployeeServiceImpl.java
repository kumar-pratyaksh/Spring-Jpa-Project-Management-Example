package com.proptiger.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proptiger.model.Employee;
import com.proptiger.model.EmployeeStatus;
import com.proptiger.model.Project;
import com.proptiger.model.Status;
import com.proptiger.repository.EmployeeDao;
import com.proptiger.services.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao repository;

	@Transactional
	public void addEmployee(Employee e) {
		repository.save(e);
	}

	@Transactional
	public void updateEmployee(Employee e, Long id) {
		Employee existingEmployee = repository.findOne(id);
		existingEmployee.setName(e.getName());
		existingEmployee.setEmail(e.getEmail());
		existingEmployee.setDepartment(e.getDepartment());
	}

	public List<Employee> findAll() {
		return repository.findAll();
	}

	public Employee findOne(Long id) {
		return repository.findOne(id);
	}

	public List<Employee> findByName(String name) {
		return repository.findByNameContainingIgnoreCase(name);
	}

	@Transactional
	public void delete(Long id) {
		Employee employee = repository.findOne(id);
		employee.setStatus(EmployeeStatus.REMOVED);
	}

	@Transactional
	public void updateScore(Long id) {
		Employee employee = repository.findOne(id);
		List<Project> list = employee.getProjects();
		int count = 0;
		double score = 0.0;
		for (Project p : list) {
			if (p.getStatus() == Status.COMPLETED) {
				count++;
				score += p.getPerformanceScore();
			}
		}
		if (count > 0)
			score /= count;
		employee.setScore(score);
	}
}
