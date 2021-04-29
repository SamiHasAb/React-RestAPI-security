package org.study.pma.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.pma.models.ProjectTask;
import org.study.pma.services.MapValidationErrorService;
import org.study.pma.services.ProjectTaskService;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

	@Autowired
	private ProjectTaskService projectTaskSer;

	@Autowired
	private MapValidationErrorService mapValidationErrorSer;

	@PostMapping("/{backlog_id}")
	public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
			@PathVariable String backlog_id, Principal principal) {

		//show delete
        //custom exception
		
		ResponseEntity<?> errorMap = mapValidationErrorSer.mapValidationService(result);
		if (errorMap != null) {
			return errorMap;
		}
		ProjectTask projectTask1 = projectTaskSer.addProjectTask(backlog_id, projectTask, principal.getName());

		return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);

	}
	
	@GetMapping("/{backlog_id}")
	    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id, Principal principal){

	        return projectTaskSer.findBacklogById(backlog_id, principal.getName());

	    }
	 @GetMapping("/{backlog_id}/{pt_id}")
	    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){
	        ProjectTask projectTask = projectTaskSer.findPTByProjectSequence(backlog_id, pt_id, principal.getName());
	 	return new ResponseEntity<ProjectTask>( projectTask, HttpStatus.OK);
	    }

	  @PatchMapping("/{backlog_id}/{pt_id}")
	    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
	                                               @PathVariable String backlog_id, @PathVariable String pt_id , Principal principal ){

	        ResponseEntity<?> errorMap = mapValidationErrorSer.mapValidationService(result);
	        if (errorMap != null) return errorMap;

	        ProjectTask updatedTask = projectTaskSer.updateByProjectSequence(projectTask,backlog_id,pt_id, principal.getName());

	        return new ResponseEntity<ProjectTask>(updatedTask,HttpStatus.OK);

	    }
	  
	  @DeleteMapping("/{backlog_id}/{pt_id}")
	    public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){
	        projectTaskSer.deletePTByProjectSequence(backlog_id, pt_id, principal.getName());

	        return new ResponseEntity<String>("Project Task "+pt_id+" was deleted successfully", HttpStatus.OK);
	    }
	 
}
