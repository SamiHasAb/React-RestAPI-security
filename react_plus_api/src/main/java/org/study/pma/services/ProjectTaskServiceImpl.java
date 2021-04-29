package org.study.pma.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.pma.exceptions.ProjectNotFoundException;
import org.study.pma.models.Backlog;
import org.study.pma.models.ProjectTask;
import org.study.pma.repositories.BacklogRepository;
import org.study.pma.repositories.ProjectRepository;
import org.study.pma.repositories.ProjectTaskRepository;

import java.util.List;

@Service
public class ProjectTaskServiceImpl implements ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;

	@Override
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

		// PTs to be added to a specific project, project != null, BL exists
		Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog(); // backlogRepository.findByProjectIdentifier(projectIdentifier);
		// set the bl to pt
		System.out.println(backlog);
		projectTask.setBacklog(backlog);
		// we want our project sequence to be like this: IDPRO-1 IDPRO-2 ...100 101
		Integer BacklogSequence = backlog.getPTSequence();
		// Update the BL SEQUENCE
		BacklogSequence++;

		backlog.setPTSequence(BacklogSequence);

		// Add Sequence to Project Task
		projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + BacklogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);

		// INITIAL priority when priority null

		// INITIAL status when status is null
		if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
			projectTask.setStatus("TO_DO");
		}

		// Fix bug with priority in Spring Boot Server, needs to check null first
		if (projectTask.getPriority() == null || projectTask.getPriority() == 0) { // In the future we need
																					// projectTask.getPriority()== 0 to
																					// handle the form
			projectTask.setPriority(3);
		}

		return projectTaskRepository.save(projectTask);

	}

	@Override
	public Iterable<ProjectTask> findBacklogById(String id, String username) {

		projectService.findProjectByIdentifier(id, username);

		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}

	@Override
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {

        projectService.findProjectByIdentifier(backlog_id, username);


		// make sure that our task exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' not found");
		}

		// make sure that the backlog/project id in the path corresponds to the right
		// project
		if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException(
					"Project Task '" + pt_id + "' does not exist in project: '" + backlog_id);
		}

		return projectTask;
	}

	@Override
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id,String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);

		projectTask = updatedTask;

		return projectTaskRepository.save(projectTask);
	}

	@Override
	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		projectTaskRepository.delete(projectTask);
	}

}