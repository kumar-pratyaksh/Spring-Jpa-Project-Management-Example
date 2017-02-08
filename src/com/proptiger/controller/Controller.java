package com.proptiger.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proptiger.dao.EmployeeDao;
import com.proptiger.dao.ProjectDao;
import com.proptiger.model.Employee;
import com.proptiger.model.EmployeeStatus;
import com.proptiger.model.Project;
import com.proptiger.model.Status;

@CrossOrigin
@RestController
public class Controller {

	@Autowired
	private ProjectDao projectServices;
	@Autowired
	private EmployeeDao employeeService;

	@RequestMapping(value = "/hello")
	public void test() {
		List<Employee> list = employeeService.findAll();
		for (Employee e : list) {
			employeeService.updateScore(e.getId());
		}
	}

	// Mappings for employee entity

	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public ResponseEntity<List<Employee>> listAll() {
		List<Employee> list = employeeService.findAll();
		Iterator<Employee> iterator = list.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getStatus() == EmployeeStatus.REMOVED)
				iterator.remove();
		}
		if (list.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<List<Employee>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/employee/id/{id}", method = RequestMethod.GET)
	public ResponseEntity<Employee> listById(@PathVariable Long id) {
		Employee e = employeeService.findOne(id);
		if (e == null)
			return new ResponseEntity<Employee>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(e, HttpStatus.OK);
	}

	@RequestMapping(value = "/employee/name/{name}", method = RequestMethod.GET)
	public ResponseEntity<List<Employee>> findByName(@PathVariable String name) {
		List<Employee> list = employeeService.findByName(name);
		if (list.isEmpty())
			return new ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/employee", method = RequestMethod.POST)
	public ResponseEntity<Void> insertEmployee(@RequestBody Employee e) {
		try {
			System.out.println(e.getDepartmentId());
			employeeService.addEmployee(e);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e1) {
			System.out.println("Error inserting employee");
			return new ResponseEntity<Void>(HttpStatus.NOT_MODIFIED);
		}
	}

	@RequestMapping(value = "/employee/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateEmployee(@PathVariable Long id, @RequestBody Employee e) {
		try {
			employeeService.updateEmployee(e, id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e1) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}
	}

	@RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE)
	public void deleteEmployee(@PathVariable Long id) {
		employeeService.delete(id);
	}

	// Mappings for project entity

	@RequestMapping(value = "/project", method = RequestMethod.GET)
	public ResponseEntity<List<Project>> findAll() {
		List<Project> list = projectServices.findAll();
		if (list.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		Iterator<Project> iterator = list.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getStatus() == Status.DELETED)
				iterator.remove();
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
	public ResponseEntity<Project> findById(@PathVariable Long id) {
		Project project = projectServices.findOne(id);
		if (project == null)
			return new ResponseEntity<Project>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(project, HttpStatus.OK);
	}

	@RequestMapping(value = "/project/name/{name}", method = RequestMethod.GET)
	public ResponseEntity<List<Project>> findProjectByName(@PathVariable String name) {
		List<Project> list = projectServices.findByName(name);
		if (list.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/project", method = RequestMethod.POST)
	public ResponseEntity<Void> insertProject(@RequestBody Project p) {
		try {
			projectServices.addProject(p);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (SQLIntegrityConstraintViolationException e) {
			// TODO: handle exception
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/project/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateProject(@RequestBody Project p, @PathVariable Long id) {
		try {
			projectServices.updateProject(p, id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (SQLIntegrityConstraintViolationException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}
	}

	@RequestMapping(value = "/project/{pId}/{eId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> addEmployeeToProject(@PathVariable Long pId, @PathVariable Long eId,
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		Employee e = employeeService.findOne(eId);
		if (e == null)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		if (date == null)
			date = new Date();
		projectServices.assignProject(pId, eId, date);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/project/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
		Project project = projectServices.deleteProject(id);
		if (project == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_MODIFIED);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/project/employee/{eId}", method = RequestMethod.GET)
	public ResponseEntity<List<Project>> getByEmployeeId(@PathVariable Long eId) {
		List<Project> list = projectServices.getByEmployeeId(eId);
		if (list.isEmpty())
			return new ResponseEntity<List<Project>>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<List<Project>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/project/{pId}/score/{score}", method = RequestMethod.PUT)
	public ResponseEntity<Void> markProjectComplete(@PathVariable Long pId, @PathVariable Double score,
			@RequestParam(required = false, name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		if (date == null)
			date = new Date();
		Long eId = projectServices.markCompleted(pId, score, date);
		employeeService.updateScore(eId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
