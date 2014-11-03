package org.wickedsource.budgeteer.persistence.person;

import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="DAILY_RATE")
public class DailyRateEntity {

    @Id
    @GeneratedValue
    private long id;

    @Temporal(TemporalType.DATE)
    private Date dateEnd;

    @Temporal(TemporalType.DATE)
    private Date dateStart;

    @ManyToOne(optional = false)
    @JoinColumn(name="personId")
    private PersonEntity person;

    @ManyToOne(optional = false)
    @JoinColumn(name="budgetId")
    private BudgetEntity budget;

    private Money rate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public BudgetEntity getBudget() {
        return budget;
    }

    public void setBudget(BudgetEntity budget) {
        this.budget = budget;
    }

    public Money getRate() {
        return rate;
    }

    public void setRate(Money rate) {
        this.rate = rate;
    }
}
