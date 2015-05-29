package org.wickedsource.budgeteer.persistence.contract;

import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="CONTRACT")
@Data
public class ContractEntity implements Serializable {

    public enum ContractType{
        T_UND_M,
        FIXED_PRICE
    }

    @Id
    @GeneratedValue
    private long id;

    @Column(name="CONTRACT_NAME", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PROJECT_ID")
    private ProjectEntity project;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name="CONTRACT_ID")
    private List<ContractFieldEntity> contractFields = new LinkedList<ContractFieldEntity>();

    @Column(name="BUDGET")
    private Money budget;

    @Column(name="INTERNAL_NUMBER")
    private String internalNumber;

    @Column(name="YEAR_OF_CONTRACT")
    private int year = 0;

    @Column(name = "CONTRACT_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private ContractType type;

    @OneToMany(mappedBy="contract")
    private List<BudgetEntity> budgets = new LinkedList<BudgetEntity>();
}
