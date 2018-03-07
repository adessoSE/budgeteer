package org.wickedsource.budgeteer.service.report;

import java.io.Serializable;

import org.wickedsource.budgeteer.service.DateRange;

import lombok.Data;

@Data
public class BudgetReportMetaInformation implements Serializable {
	private DateRange overallTimeRange;
	private DateRange monthlyTimeRange;
}
