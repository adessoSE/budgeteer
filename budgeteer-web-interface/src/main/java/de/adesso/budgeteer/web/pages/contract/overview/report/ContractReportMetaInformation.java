package de.adesso.budgeteer.web.pages.contract.overview.report;

import de.adesso.budgeteer.service.template.Template;
import de.adesso.budgeteer.web.pages.contract.overview.report.form.FormattedDate;
import lombok.Data;

import java.io.Serializable;

@Data
public class ContractReportMetaInformation implements Serializable {
	private FormattedDate selectedMonth;
	private Template template;
}
