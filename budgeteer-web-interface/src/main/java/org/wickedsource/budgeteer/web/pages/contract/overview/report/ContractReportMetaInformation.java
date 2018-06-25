package org.wickedsource.budgeteer.web.pages.contract.overview.report;

import java.io.Serializable;

import lombok.Data;

import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.web.pages.contract.overview.report.form.FormattedDate;

@Data
public class ContractReportMetaInformation implements Serializable {
	private FormattedDate selectedMonth;
	private Template template;
}
