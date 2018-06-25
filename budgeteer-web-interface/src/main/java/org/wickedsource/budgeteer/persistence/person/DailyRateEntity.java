package org.wickedsource.budgeteer.persistence.person;

import java.util.Date;

import javax.persistence.*;

import lombok.Data;

import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;

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
