package org.wickedsource.budgeteer.web.pages.budgets.overview.report;


import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.report.BudgetReportMetaInformation;
import org.wickedsource.budgeteer.service.report.ReportService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.budgets.overview.report.form.BudgetReportForm;

@Mount({"budgets/report"})
public class BudgetReportPage extends DialogPageWithBacklink {

    @SpringBean
    private ReportService service;

    /**
     * Use this constructor to create a page with a form to create a new budget.
     */
    public BudgetReportPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        Form<BudgetReportMetaInformation> form = new BudgetReportForm("form");
        addComponents(form);
    }

    private void addComponents(Form<BudgetReportMetaInformation> form) {
        add(createBacklink("cancelButton1"));
        form.add(createBacklink("cancelButton2"));
        add(form);
    }
}