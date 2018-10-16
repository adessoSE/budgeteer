package org.wickedsource.budgeteer.web.pages.budgets.manualRecords;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.persistence.record.AddManualRecordData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.budgets.manualRecords.form.AddManualRecordForm;

@Mount({"budgets/manual/${id}", "budgets/manual"})
public class AddManualRecordsPage extends DialogPageWithBacklink {
    @SpringBean
    private BudgetService service;

    public AddManualRecordsPage(PageParameters parameters, Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(parameters, backlinkPage, backlinkParameters);
        Form<AddManualRecordData> form = new AddManualRecordForm("form", backlinkParameters);
        addComponents(form);
    }

    public AddManualRecordsPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        Form<AddManualRecordData> form = new AddManualRecordForm("form",  backlinkParameters);
        addComponents(form);
    }

    private void addComponents(Form<AddManualRecordData> form) {
        add(createBacklink("cancelButton1"));
        form.add(createBacklink("cancelButton2"));
        add(form);
    }
}
