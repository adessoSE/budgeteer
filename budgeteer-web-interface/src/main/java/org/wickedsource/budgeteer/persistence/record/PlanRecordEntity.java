package org.wickedsource.budgeteer.persistence.record;

import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name="PLAN_RECORD")
public class PlanRecordEntity {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private PersonEntity person;

    @ManyToOne(optional = false)
    private BudgetEntity budget;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int day;

    @Column(nullable = false)
    private int week;

    @Column(nullable = false)
    private int plannedMinutes;

    @Column(nullable = false)
    private Money dailyRate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        this.date = date;
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
        this.day = c.get(Calendar.DAY_OF_MONTH);
        this.week = c.get(Calendar.WEEK_OF_YEAR);
    }

    public int getPlannedMinutes() {
        return plannedMinutes;
    }

    public void setPlannedMinutes(int plannedMinutes) {
        this.plannedMinutes = plannedMinutes;
    }

    public Money getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Money dailyRate) {
        this.dailyRate = dailyRate;
    }
}
