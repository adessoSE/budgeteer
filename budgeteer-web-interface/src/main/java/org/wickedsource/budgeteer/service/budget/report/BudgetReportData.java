package org.wickedsource.budgeteer.service.budget.report;

import lombok.Data;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplateSerializable;

import java.util.Date;
import java.util.List;

@Data
class BudgetReportData {
	private long id;
	private Date from;
	private Date until;
	private String name;
	private double spentNet;
	private double spentGross;
	private double hoursAggregated;
	private double budgetRemainingNet;
	private double budgetRemainingGross;
	private Double progress;
	private List<? extends SheetTemplateSerializable> attributes;
}
