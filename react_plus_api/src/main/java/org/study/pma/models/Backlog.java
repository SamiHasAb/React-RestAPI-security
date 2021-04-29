package org.study.pma.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class Backlog {

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private Integer PTSequence = 0;
	    private String projectIdentifier;

	    //OneToOne with project
	    @OneToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name="project_id",nullable = false)
	    @JsonIgnore
	    private Project project;
	   
	    //OneToMany projecttasks
	    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "backlog", orphanRemoval = true)
	    private List<ProjectTask> projectTasks = new ArrayList<>();
	    //initiate the array here
	    
	    public Backlog() {
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public Integer getPTSequence() {
	        return PTSequence;
	    }

	    public void setPTSequence(Integer PTSequence) {
	        this.PTSequence = PTSequence;
	    }

	    public String getProjectIdentifier() {
	        return projectIdentifier;
	    }

	    public void setProjectIdentifier(String projectIdentifier) {
	        this.projectIdentifier = projectIdentifier;
	    }

		public Project getProject() {
			return project;
		}

		public void setProject(Project project) {
			this.project = project;
		}

		public List<ProjectTask> getProjectTasks() {
			return projectTasks;
		}

		public void setProjectTasks(List<ProjectTask> projectTasks) {
			this.projectTasks = projectTasks;
		}
	
}
