package org.wickedsource.budgeteer.persistence.contract;

import lombok.Data;
import org.wickedsource.budgeteer.persistence.user.UserEntity;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CONTRACT_SORTING")
public class ContractSortingEntity {
    @Id
    @SequenceGenerator(name = "SEQ_CONTRACT_SORTING_ID", sequenceName = "SEQ_CONTRACT_SORTING_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONTRACT_SORTING_ID")
    private long id;

    @Column(name = "SORTING_INDEX")
    private Integer sortingIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACT_ID")
    private ContractEntity contract;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;
}
