package org.wickedsource.budgeteer.persistence.budget;

import javax.persistence.*;

@Entity
@Table(name = "BUDGET_TAG")
//        uniqueConstraints = {
//                @UniqueConstraint(name = "UNIQUE_BUDGET_TAG", columnNames = {"tag", "budgetId"}),
//        })

public class BudgetTagEntity {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "budgetId")
    private BudgetEntity budget;

    private String tag;

    public BudgetTagEntity() {
    }

    public BudgetTagEntity(String tag) {
        this.tag = tag;
    }

    public BudgetEntity getBudget() {
        return budget;
    }

    public void setBudget(BudgetEntity budget) {
        this.budget = budget;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
