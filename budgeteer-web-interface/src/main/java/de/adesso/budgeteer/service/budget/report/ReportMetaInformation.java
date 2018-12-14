package de.adesso.budgeteer.service.budget.report;

import de.adesso.budgeteer.service.template.Template;
import lombok.Data;
import de.adesso.budgeteer.service.DateRange;

import java.io.Serializable;

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
