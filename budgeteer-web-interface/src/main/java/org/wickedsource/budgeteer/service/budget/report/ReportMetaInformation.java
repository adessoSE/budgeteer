package org.wickedsource.budgeteer.service.budget.report;

import java.io.Serializable;
import java.util.List;

import org.wickedsource.budgeteer.service.DateRange;

import lombok.Data;
import org.wickedsource.budgeteer.service.template.Template;

@Data
public class ReportMetaInformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DateRange overallTimeRange;
	private DateRange monthlyTimeRange;
	private Template template;
}
