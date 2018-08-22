package org.wickedsource.budgeteer.service.contract.report;

import lombok.Data;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplateSerializable;

import java.util.Date;
import java.util.List;

@Data
class ContractReportData {
	private long id;
	private String contract;
	private String contractId;
	private Date from;
	private Date until;
	private double budgetspentNet;
	private double budgetLeftNet;
	private double budgetTotalNet;
	private double budgetSpentGross;
	private double budgetLeftGross;
	private double budgetTotalGross;
	private Double taxRate;
	private Double progress;
	private List<? extends SheetTemplateSerializable> attributes;
}
