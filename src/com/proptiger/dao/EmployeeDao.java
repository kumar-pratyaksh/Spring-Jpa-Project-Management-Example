package com.proptiger.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proptiger.model.Employee;
import com.proptiger.model.EmployeeStatus;
import com.proptiger.model.Project;
import com.proptiger.model.Status;
import com.proptiger.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeDao {

	@Autowired
	private EmployeeRepository repository;

	public void addEmployee(Employee e) {
		// List<Project> projects = e.getProjects();
		// double score = 0.0;
		// if (!projects.isEmpty()) {
		//
		// for (int i = 0; i < projects.size(); i++) {
		// score += projects.get(i).getPerformanceScore();
		// }
		// score /= projects.size();
		// }
		// e.setScore(score);
		repository.save(e);
	}

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

	public void delete(Long id) {
		Employee employee = repository.findOne(id);
		employee.setStatus(EmployeeStatus.REMOVED);
	}

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
