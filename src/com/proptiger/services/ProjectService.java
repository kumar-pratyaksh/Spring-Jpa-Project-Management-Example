package com.proptiger.services;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;

import com.proptiger.model.Project;

public interface ProjectService {

	public List<Project> findAll();

	public Project findOne(Long id);

	public List<Project> findByName(String name);

	public void addProject(Project p) throws SQLIntegrityConstraintViolationException;

	public void updateProject(Project p, Long id) throws SQLIntegrityConstraintViolationException;

	public void assignProject(Long projectId, Long employeeId, Date date);

	public Long markCompleted(Long projectId, Double score, Date date);

	public List<Project> getByEmployeeId(Long id);

	public Project deleteProject(Long id);
}
