package org.study.pma.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.study.pma.models.Project;
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	Project findByProjectIdentifier (String projectIdent);
	
    Iterable<Project> findAllByProjectLeader(String username);
    

}
