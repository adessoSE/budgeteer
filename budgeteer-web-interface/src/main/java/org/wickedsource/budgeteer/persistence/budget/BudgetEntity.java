package org.wickedsource.budgeteer.persistence.budget;

import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.person.DailyRateEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.record.PlanRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BUDGET",
        uniqueConstraints = {
                @UniqueConstraint(name = "UNIQUE_IMPORT_KEY_PER_PROJECT", columnNames = {"PROJECT_ID", "IMPORT_KEY"})
        })
@Data
public class BudgetEntity {

    @Id
    @SequenceGenerator(name="SEQ_BUDGET_ID", sequenceName="SEQ_BUDGET_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BUDGET_ID")
    private long id;

    @Column(nullable = false)
    private String name;

    private Money total;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PROJECT_ID")
    private ProjectEntity project;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "budget")
    private List<BudgetTagEntity> tags = new ArrayList<BudgetTagEntity>();

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WorkRecordEntity> workRecords = new ArrayList<WorkRecordEntity>();

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PlanRecordEntity> planRecords = new ArrayList<PlanRecordEntity>();

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DailyRateEntity> dailyRates = new ArrayList<DailyRateEntity>();

    @Column(nullable = false, name = "IMPORT_KEY")
    private String importKey;

    @ManyToOne
    @JoinColumn(name="CONTRACT_ID")
    private ContractEntity contract;

}
