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

import com.proptiger.model.Employee;
import com.proptiger.model.EmployeeStatus;
import com.proptiger.model.Project;
import com.proptiger.model.Status;
import com.proptiger.services.EmployeeService;
import com.proptiger.services.ProjectService;

@RestController
public class Controller {

	@Autowired
	private ProjectService projectService;
	@Autowired
	private EmployeeService employeeService;

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

	@RequestMapping(value = "/employee/{id:[0-9]+}", method = RequestMethod.GET)
	public ResponseEntity<Employee> listById(@PathVariable Long id) {
		Employee e = employeeService.findOne(id);
		if (e == null)
			return new ResponseEntity<Employee>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(e, HttpStatus.OK);
	}

	@RequestMapping(value = "/employee/{name:[a-zA-Z-]+}", method = RequestMethod.GET)
	public ResponseEntity<List<Employee>> findByName(@PathVariable String name) {
		List<Employee> list = employeeService.findByName(name);
		if (list.isEmpty())
			return new ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/employee", method = RequestMethod.POST)
	public ResponseEntity<Void> insertEmployee(@RequestBody Employee e) {
		try {
			employeeService.addEmployee(e);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e1) {
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
		List<Project> list = projectService.findAll();
		if (list.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		Iterator<Project> iterator = list.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getStatus() == Status.DELETED)
				iterator.remove();
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/project/{id:[0-9]+}", method = RequestMethod.GET)
	public ResponseEntity<Project> findById(@PathVariable Long id) {
		Project project = projectService.findOne(id);
		if (project == null)
			return new ResponseEntity<Project>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(project, HttpStatus.OK);
	}

	@RequestMapping(value = "/project/{name:[a-zA-Z-]+}", method = RequestMethod.GET)
	public ResponseEntity<List<Project>> findProjectByName(@PathVariable String name) {
		List<Project> list = projectService.findByName(name);
		if (list.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/project", method = RequestMethod.POST)
	public ResponseEntity<Void> insertProject(@RequestBody Project p) {
		try {
			projectService.addProject(p);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (SQLIntegrityConstraintViolationException e) {
			// TODO: handle exception
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/project/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateProject(@RequestBody Project p, @PathVariable Long id) {
		try {
			projectService.updateProject(p, id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (SQLIntegrityConstraintViolationException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}
	}

	// Not working without @CrossOrigin
	@CrossOrigin
	@RequestMapping(value = "/project", method = RequestMethod.PUT)
	public ResponseEntity<Void> addEmployeeToProject(@RequestParam(name = "pid") Long pId,
			@RequestParam(name = "eid") Long eId,
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		Employee e = employeeService.findOne(eId);
		if (e == null)
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		if (date == null)
			date = new Date();
		projectService.assignProject(pId, eId, date);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/project/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
		Project project = projectService.deleteProject(id);
		if (project == null)
			return new ResponseEntity<Void>(HttpStatus.NOT_MODIFIED);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/project-by-employee", method = RequestMethod.GET)
	public ResponseEntity<List<Project>> getByEmployeeId(@RequestParam(name = "eid", required = true) Long eId) {
		List<Project> list = projectService.getByEmployeeId(eId);
		if (list.isEmpty())
			return new ResponseEntity<List<Project>>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<List<Project>>(list, HttpStatus.OK);
	}

	// Not working without @CrossOrigin
	@CrossOrigin
	@RequestMapping(value = "/complete_project", method = RequestMethod.PUT)
	public ResponseEntity<Void> markProjectComplete(@RequestParam(name = "pid") Long pId,
			@RequestParam(name = "score") Double score,
			@RequestParam(required = false, name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		if (date == null)
			date = new Date();
		Long eId = projectService.markCompleted(pId, score, date);
		employeeService.updateScore(eId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
