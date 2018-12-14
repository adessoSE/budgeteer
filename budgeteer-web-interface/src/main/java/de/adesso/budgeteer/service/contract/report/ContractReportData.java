package de.adesso.budgeteer.service.contract.report;

import de.adesso.budgeteer.SheetTemplate.SheetTemplateSerializable;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
class ContractReportData {
	private long id;
	private String contract;
	private String contractId;
	private Date from;
	private Date until;
	private double budgetSpent_net;
	private double budgetLeft_net;
	private double budgetTotal_net;
	private double budgetSpent_gross;
	private double budgetLeft_gross;
	private double budgetTotal_gross;
	private Double taxRate;
	private Double progress;
	private List<? extends SheetTemplateSerializable> attributes;
}
