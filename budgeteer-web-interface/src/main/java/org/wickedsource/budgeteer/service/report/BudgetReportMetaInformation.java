package org.wickedsource.budgeteer.service.report;

import java.io.Serializable;

import org.wickedsource.budgeteer.service.DateRange;

import lombok.Data;

@Data
public class BudgetReportMetaInformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DateRange overallTimeRange;
	private DateRange monthlyTimeRange;
}
