package org.wickedsource.budgeteer.service.person;

import java.util.Date;

import lombok.Data;

import org.joda.money.Money;

@Data
public class PersonDetailData {

	private String name;
	private Money averageDailyRate;
	private Date firstBookedDate;
	private Date lastBookedDate;
	private Double hoursBooked;
	private Money budgetBurned;

}
