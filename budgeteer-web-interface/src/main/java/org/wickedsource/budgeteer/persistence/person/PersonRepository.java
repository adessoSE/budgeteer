package org.wickedsource.budgeteer.persistence.person;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends CrudRepository<PersonEntity, Long> {

    public List<PersonEntity> findByProjectId(long projectId);

    @Query("select new org.wickedsource.budgeteer.persistence.person.PersonBaseDataBean(p.id, p.name, sum(r.minutes * r.dailyRate) / sum(r.minutes), max(r.date)) from WorkRecordEntity r join r.person p where p.project.id=:projectId group by p.id, p.name")
    public List<PersonBaseDataBean> findBaseDataByProjectId(@Param("projectId") long projectId);

    @Query("select new org.wickedsource.budgeteer.persistence.person.PersonBaseDataBean(p.id, p.name, sum(r.minutes * r.dailyRate) / sum(r.minutes), max(r.date)) from WorkRecordEntity r join r.person p where p.id=:personId group by p.id, p.name")
    public PersonBaseDataBean findBaseDataByPersonId(@Param("personId") long personId);

    @Query("select new org.wickedsource.budgeteer.persistence.person.PersonDetailDataBean(p.id, p.name, sum(r.minutes * r.dailyRate) / sum(r.minutes), min(r.date), max(r.date), sum(r.minutes) / 60.0, sum(r.minutes * r.dailyRate) / 60 / 8) from WorkRecordEntity r join r.person p where p.id=:personId group by p.id, p.name")
    public PersonDetailDataBean findDetailDataByPersonId(@Param("personId") long personId);

    @Query("select p from PersonEntity p left join fetch p.dailyRates r left join fetch r.budget where p.id=:personId")
    public PersonEntity findOneFetchDailyRates(@Param("personId") long personId);

}
