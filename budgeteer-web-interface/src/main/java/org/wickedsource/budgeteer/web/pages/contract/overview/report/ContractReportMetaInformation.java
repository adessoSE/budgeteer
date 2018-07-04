package org.wickedsource.budgeteer.web.pages.contract.overview.report;

import lombok.Data;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.web.pages.contract.overview.report.form.FormattedDate;

import java.io.Serializable;

@Data
public class ContractReportMetaInformation implements Serializable {
	private FormattedDate selectedMonth;
	private Template template;
}
