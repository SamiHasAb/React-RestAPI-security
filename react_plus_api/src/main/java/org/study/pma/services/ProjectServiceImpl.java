package org.study.pma.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.pma.exceptions.ProjectIdException;
import org.study.pma.exceptions.ProjectNotFoundException;
import org.study.pma.models.Backlog;
import org.study.pma.models.Project;
import org.study.pma.models.User;
import org.study.pma.repositories.BacklogRepository;
import org.study.pma.repositories.ProjectRepository;
import org.study.pma.repositories.UserRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository proRep;

	@Autowired
	private BacklogRepository backlogRep;

	@Autowired
	private UserRepository userRep;

	@Override
	public Project saveOrUpdate(Project project, String username) {

		
	     if(project.getId() != null){
	            Project existingProject = proRep.findByProjectIdentifier(project.getProjectIdentifier());
	            if(existingProject !=null &&(!existingProject.getProjectLeader().equals(username))){
	                throw new ProjectNotFoundException("Project not found in your account");
	            }else if(existingProject == null){
	                throw new ProjectNotFoundException("Project with ID: '"+project.getProjectIdentifier()+"' cannot be updated because it doesn't exist");
	            }
	        }

		try {

			User user = userRep.findByUsername(username);

			project.setUser(user);
			project.setProjectLeader(user.getUsername());

			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}

			if (project.getId() != null) {
				project.setBacklog(backlogRep.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}

			return proRep.save(project);
		} catch (Exception e) {
			throw new ProjectIdException(
					"Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
		}

	}

	@Override
    public Project findProjectByIdentifier(String projectIdent, String username){

		Project project = proRep.findByProjectIdentifier(projectIdent.toUpperCase());

		if (project == null)
			throw new ProjectIdException(
					"Can not find project with ID : " + projectIdent + ".This project does not exist");
		
		   if(!project.getProjectLeader().equals(username)){
	            throw new ProjectNotFoundException("Project not found in your account");
	        }
		
		return project;
	}

	@Override
	public void deletProjectByIdentifier(String projectIdent ,String username) {

		Project project = proRep.findByProjectIdentifier(projectIdent.toUpperCase());
		if (project == null)
			throw new ProjectIdException(
					"Can not find project with ID : " + projectIdent + ".This project does not exist");
		proRep.delete(findProjectByIdentifier(projectIdent, username));


	}

	@Override
	public Iterable<Project> getAllprojects() {
		return proRep.findAll();
	}
	
	   public Iterable<Project> findAllProjects(String username){
	        return proRep.findAllByProjectLeader(username);

	   }
}
