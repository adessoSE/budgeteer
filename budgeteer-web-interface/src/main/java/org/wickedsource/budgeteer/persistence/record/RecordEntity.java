package org.wickedsource.budgeteer.persistence.record;

import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class RecordEntity {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PERSON_ID")
    private PersonEntity person;

    @ManyToOne(optional = false)
    @JoinColumn(name = "BUDGET_ID")
    private BudgetEntity budget;

    @Temporal(TemporalType.DATE)
    @Column(name="RECORD_DATE", nullable = false)
    private Date date;

    @Column(name="RECORD_YEAR", nullable = false)
    private int year;

    @Column(name="RECORD_MONTH", nullable = false)
    private int month;

    @Column(name="RECORD_DAY", nullable = false)
    private int day;

    @Column(name="RECORD_WEEK", nullable = false)
    private int week;

    @Column(nullable = false)
    private int minutes;

    @Column(nullable = false)
    private Money dailyRate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "IMPORT_ID")
    private ImportEntity importRecord;


    public void setDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        this.date = date;
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
        this.day = c.get(Calendar.DAY_OF_MONTH);
        this.week = c.get(Calendar.WEEK_OF_YEAR);
    }
}
