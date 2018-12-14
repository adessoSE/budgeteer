package de.adesso.budgeteer.service.budget.report;

import de.adesso.budgeteer.SheetTemplate.SheetTemplateSerializable;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
class BudgetReportData {
	private long id;
	private Date from;
	private Date until;
	private String name;
	private double spent_net;
	private double spent_gross;
	private double hoursAggregated;
	private double budgetRemaining_net;
	private double budgetRemaining_gross;
	private Double progress;
	private List<? extends SheetTemplateSerializable> attributes;
}
