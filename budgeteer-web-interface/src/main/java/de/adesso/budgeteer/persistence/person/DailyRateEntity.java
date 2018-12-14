package de.adesso.budgeteer.persistence.person;

import lombok.Data;
import org.joda.money.Money;
import de.adesso.budgeteer.persistence.budget.BudgetEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="DAILY_RATE")
@Data
public class DailyRateEntity {

    @Id
    @GeneratedValue
    private long id;

    @Temporal(TemporalType.DATE)
    private Date dateEnd;

    @Temporal(TemporalType.DATE)
    private Date dateStart;

    @ManyToOne(optional = false)
    @JoinColumn(name="PERSON_ID")
    private PersonEntity person;

    @ManyToOne(optional = false)
    @JoinColumn(name="BUDGET_ID")
    private BudgetEntity budget;

    private Money rate;
}
