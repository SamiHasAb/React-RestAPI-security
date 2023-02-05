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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.pma.models.Project;
import org.study.pma.services.MapValidationErrorService;
import org.study.pma.services.ProjectService;


@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

	@Autowired
	private ProjectService projSer;
	
	@Autowired
	private MapValidationErrorService mapValErrorSer;
	
	
	
	@PostMapping("")
	public ResponseEntity<?> createNewProject (@Valid @RequestBody Project project, BindingResult result , Principal principal ){
		
		ResponseEntity<?> errorMap = mapValErrorSer.mapValidationService(result);
		
        if(errorMap!=null) return errorMap;
        
		Project project1 = projSer.saveOrUpdate(project, principal.getName());

		return new ResponseEntity<Project>(project1 , HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{projectIdent}")
	public ResponseEntity<?>  getProjectById(@PathVariable String projectIdent, Principal principal){
		
		Project project = projSer.findProjectByIdentifier(projectIdent,  principal.getName());
		
		return new ResponseEntity<Project>(project,HttpStatus.OK);
	}
	
	 @GetMapping("/all")
	    public Iterable<Project> getAllProjects(Principal principal){
		 return projSer.findAllProjects(principal.getName());
		 }

	 
	 @DeleteMapping("/{projectIdent}")
	 public ResponseEntity<?>  deletePrject(@PathVariable String projectIdent , Principal principal ){
		
		 projSer.deleteProjectByIdentifier(projectIdent , principal.getName()  );
		 
		 return new ResponseEntity<String>("project with ID:"+projectIdent+" was deleted",HttpStatus.OK);
		 
	 }
	
	
}
