package org.wickedsource.budgeteer.persistence.contract;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ContractSortingRepository extends CrudRepository<ContractSortingEntity, Long> {
    @Query("select cs.sortingIndex from ContractSortingEntity cs where cs.contract.id = :contractId and cs.user.id = :userId")
    Integer getSortingIndex(@Param("contractId") long contractId, @Param("userId") long userId);

    @Modifying
    @Query("update ContractSortingEntity cs set cs.sortingIndex=:index where cs.user.id=:userId and cs.contract.id =:contractId")
    void setSortingIndex(@Param("contractId") long contractId, @Param("userId") long userId, @Param("index") Integer index);

    @Query("select cs from ContractSortingEntity cs where cs.contract.id = :contractId and cs.user.id = :userId")
    ContractSortingEntity findByContractIdAndUserId(@Param("contractId") long contractId, @Param("userId") long userId);
}
