package org.wickedsource.budgeteer.persistence.budget;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "BUDGET_TAG", indexes = {@Index(name="BUDGET_TAG_BUDGET_ID_IDX", columnList = "BUDGET_ID")})
@Data
@NoArgsConstructor
public class BudgetTagEntity {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "BUDGET_ID")
    private BudgetEntity budget;

    private String tag;


    public BudgetTagEntity(String tag) {
        this.tag = tag;
    }

}
