package com.proptiger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proptiger.model.Project;

public interface ProjectDao extends JpaRepository<Project, Long> {
	public List<Project> findByNameContainingIgnoreCase(String name);

	public List<Project> findByEmployeeIdOrderByStatusAsc(Long id);
}
