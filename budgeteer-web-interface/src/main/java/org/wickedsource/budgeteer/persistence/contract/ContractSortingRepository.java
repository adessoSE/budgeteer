package org.wickedsource.budgeteer.persistence.contract;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ContractSortingRepository extends CrudRepository<ContractSortingEntity, Long> {
    @Query("select cs.sortingIndex from ContractSortingEntity cs where cs.contract.id = :contractId and cs.user.id = :userId")
    Integer getSortingIndex(@Param("contractId") long contractId, @Param("userId") long userId);

    @Query("select cs from ContractSortingEntity cs where cs.contract.id = :contractId and cs.user.id = :userId")
    ContractSortingEntity findByContractIdAndUserId(@Param("contractId") long contractId, @Param("userId") long userId);

    @Modifying
    @Query("delete from ContractSortingEntity c where c.id in (select s.id from ContractSortingEntity s where s.contract.project.id = :projectId)")
    void deleteByProjectId(@Param("projectId") long projectId);
}
