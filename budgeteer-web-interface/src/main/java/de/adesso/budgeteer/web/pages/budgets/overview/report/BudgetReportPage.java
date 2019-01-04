package de.adesso.budgeteer.web.pages.budgets.overview.report;

import de.adesso.budgeteer.service.budget.report.ReportMetaInformation;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import de.adesso.budgeteer.web.pages.budgets.overview.report.form.BudgetReportForm;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@Mount({"budgets/report"})
public class BudgetReportPage extends DialogPageWithBacklink {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public BudgetReportPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        Form<ReportMetaInformation> form = new BudgetReportForm("form");
        addComponents(form);
    }

    private void addComponents(Form<ReportMetaInformation> form) {
        add(createBacklink("cancelButton1"));
        form.add(createBacklink("cancelButton2"));
        add(form);
    }
}