package org.study.pma.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.study.pma.models.Backlog;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long>{
	
		Backlog findByProjectIdentifier(String identifier);
}
