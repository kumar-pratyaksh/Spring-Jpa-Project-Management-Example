package com.proptiger.dao;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proptiger.model.Project;
import com.proptiger.model.Status;
import com.proptiger.repository.ProjectRepository;

@Service
@Transactional
public class ProjectDao {

	@Autowired
	private ProjectRepository repository;

	public List<Project> findAll() {
		return repository.findAll(new Sort(Sort.Direction.DESC, "status"));
	}

	public Project findOne(Long id) {
		return repository.findOne(id);
	}

	public List<Project> findByName(String name) {
		return repository.findByNameContainingIgnoreCase(name);
	}

	public void addProject(Project p) throws SQLIntegrityConstraintViolationException {
		repository.save(p);
	}

	public void updateProject(Project p, Long id) throws SQLIntegrityConstraintViolationException {
		Project existing = repository.findOne(id);
		existing.setEndDate(p.getEndDate());
		existing.setDescription(p.getDescription());
		existing.setName(p.getName());
		// existing.set
	}

	public void assignProject(Long projectId, Long employeeId, Date date) {
		Project existing = repository.findOne(projectId);
		existing.setStartDate(date);
		existing.setStatus(Status.ASSIGNED);
		existing.setEmployeeId(employeeId);
	}

	public Long markCompleted(Long projectId, Double score, Date date) {
		Project existing = repository.findOne(projectId);
		existing.setStatus(Status.COMPLETED);
		existing.setEndDate(date);
		existing.setPerformanceScore(score);
		return existing.getEmployee().getId();
	}

	public List<Project> getByEmployeeId(Long id) {
		return repository.findByEmployeeIdOrderByStatusAsc(id);
	}

	public Project deleteProject(Long id) {
		Project project = repository.findOne(id);
		if (project == null)
			return null;
		project.setStatus(Status.DELETED);
		return project;
	}
}
