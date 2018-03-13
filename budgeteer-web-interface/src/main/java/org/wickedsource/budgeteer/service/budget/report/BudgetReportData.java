package org.wickedsource.budgeteer.service.budget.report;

import java.util.Date;
import java.util.List;

import org.wickedsource.budgeteer.SheetTemplate.SheetTemplateSerializable;

import lombok.Data;

@Data
public class BudgetReportData {
	private long id;
	private Date from;
	private Date until;
	private String name;
	private double spent_net;
	private double spent_gross;
	private double hoursAggregated;
	private double budgetRemaining_net;
	private double budgetRemaining_gross;
	private double progress;
	private List<? extends SheetTemplateSerializable> attributes;
}
