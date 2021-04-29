package org.study.pma.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.study.pma.models.ProjectTask;

@Repository
public interface ProjectTaskRepository extends CrudRepository< ProjectTask, Long> {
	
    List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);

    ProjectTask findByProjectSequence(String sequence);


}
