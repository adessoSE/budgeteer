package org.wickedsource.budgeteer.persistence.person;

import java.util.Date;

public class PersonDetailDataBean {

	private Long id;

	private String name;

	private Long averageDailyRateInCents;

	private Date firstBookedDate;

	private Date lastBookedDate;

	private Double hoursBooked;

	private Long budgetBurnedInCents;

	public PersonDetailDataBean(
			Long id,
			String name,
			Long averageDailyRateInCents,
			Date firstBookedDate,
			Date lastBookedDate,
			Double hoursBooked,
			Long budgetBurnedInCents) {
		this.id = id;
		this.name = name;
		this.averageDailyRateInCents = averageDailyRateInCents;
		this.firstBookedDate = firstBookedDate;
		this.lastBookedDate = lastBookedDate;
		this.hoursBooked = hoursBooked;
		this.budgetBurnedInCents = budgetBurnedInCents;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getAverageDailyRateInCents() {
		return averageDailyRateInCents;
	}

	public Date getFirstBookedDate() {
		return firstBookedDate;
	}

	public Date getLastBookedDate() {
		return lastBookedDate;
	}

	public Double getHoursBooked() {
		return hoursBooked;
	}

	public Long getBudgetBurnedInCents() {
		return budgetBurnedInCents;
	}
}
