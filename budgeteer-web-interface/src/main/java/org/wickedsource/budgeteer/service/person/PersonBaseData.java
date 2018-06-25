package org.wickedsource.budgeteer.service.person;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.money.Money;

@NoArgsConstructor
@Data
public class PersonBaseData implements Serializable {

	private Long id;
	private String name;
	private Money averageDailyRate;
	private Date lastBooked;

	public PersonBaseData(Long id) {
		this.id = id;
	}
}
