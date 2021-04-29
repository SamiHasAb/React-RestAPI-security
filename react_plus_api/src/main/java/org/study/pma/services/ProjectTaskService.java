package org.study.pma.services;

import java.security.Principal;

import org.study.pma.models.ProjectTask;

public interface ProjectTaskService {
	
    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask , String username);

    public Iterable<ProjectTask>findBacklogById(String id, String username);
    
    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username);
    
    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id,String username);

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username);
}
