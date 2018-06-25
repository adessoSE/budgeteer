package org.wickedsource.budgeteer.persistence.record;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissingDailyRateBean {

	private long personId;

	private String personName;

	private Date startDate;

	private Date endDate;
}
