package org.wickedsource.budgeteer.persistence.record;

import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WORK_RECORD")
public class WorkRecordEntity {

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
    private int spentMinutes;

    @Column(nullable = false)
    private Money dailyRate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "importId")
    private ImportEntity importRecord;

    public ImportEntity getImportRecord() {
        return importRecord;
    }

    public void setImportRecord(ImportEntity importRecord) {
        this.importRecord = importRecord;
    }

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
        this.date = date;
    }

    public int getSpentMinutes() {
        return spentMinutes;
    }

    public void setSpentMinutes(int spentMinutes) {
        this.spentMinutes = spentMinutes;
    }

    public Money getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Money dailyRate) {
        this.dailyRate = dailyRate;
    }
}
