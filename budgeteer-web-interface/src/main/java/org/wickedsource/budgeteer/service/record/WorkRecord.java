package org.wickedsource.budgeteer.service.record;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.joda.money.Money;

@Data
public class WorkRecord implements Serializable {
	private long id;
	private String budgetName;
	private String personName;
	private Date date;
	private double hours;
	private Money budgetBurned;
	private Money dailyRate;

	private boolean editedManually;

}
