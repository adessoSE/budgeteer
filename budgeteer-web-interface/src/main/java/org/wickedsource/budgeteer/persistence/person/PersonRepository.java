package org.wickedsource.budgeteer.persistence.person;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<PersonEntity, Long> {

    public List<PersonEntity> findByProjectId(long projectId);

}
