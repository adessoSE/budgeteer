package org.wickedsource.budgeteer.persistence.person;

import java.io.Serializable;
import java.util.Date;

public class PersonBaseDataBean implements Serializable {

	private Long id;

	private String name;

	private Long averageDailyRateInCents;

	private Date lastBookedDate;

	public PersonBaseDataBean(Long id, String name, Long averageDailyRateInCents, Date lastBookedDate) {
		this.id = id;
		this.name = name;
		this.averageDailyRateInCents = averageDailyRateInCents;
		this.lastBookedDate = lastBookedDate;
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

	public Date getLastBookedDate() {
		return lastBookedDate;
	}
}
