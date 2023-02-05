package org.study.pma.services;

import org.study.pma.models.Project;

public interface ProjectService {

	public Project saveOrUpdate(Project project, String username);

	public Project findProjectByIdentifier( String projectIdent, String username);

	public void deleteProjectByIdentifier(String projectIdent , String username);

	public Iterable<Project> getAllProjects();

	public Iterable<Project> findAllProjects(String username);

}
