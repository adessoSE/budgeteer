package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.add;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.persistence.manualRecord.ManualRecord;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.budgets.manualRecords.add.form.AddManualRecordForm;

@Mount({"budgets/details/manuals/add/${id}", "budgets/details/manuals/add"})
public class AddManualRecordPage extends DialogPageWithBacklink {
    public AddManualRecordPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        Form<ManualRecord> form = new AddManualRecordForm("form",backlinkParameters);
        addComponents(form);
    }

    private void addComponents(Form<ManualRecord> form) {
        add(createBacklink("cancelButton1"));
        form.add(createBacklink("cancelButton2"));
        add(form);
    }
}
