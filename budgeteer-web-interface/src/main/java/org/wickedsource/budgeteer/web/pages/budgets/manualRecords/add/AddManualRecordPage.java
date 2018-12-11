package org.wickedsource.budgeteer.web.pages.budgets.manualRecords.add;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecord;
import org.wickedsource.budgeteer.service.manualRecord.ManualRecordService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.budgets.manualRecords.add.form.AddManualRecordForm;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount({"budgets/details/manuals/add/${RecordId}", "budgets/details/manuals/add"})
public class AddManualRecordPage extends DialogPageWithBacklink {
    private boolean isEditing;

    @SpringBean
    private ManualRecordService service;

    /**
     * Use this constructor when a new record should be added
     */
    public AddManualRecordPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        isEditing = false;
        Form<ManualRecord> form = new AddManualRecordForm("form",backlinkParameters);
        addComponents(form);
    }

    /**
     * Use this constructor when a existing record should be edited
     *
     * @param parameters page parameters containing the id of the manual record to edit.
     */
    public AddManualRecordPage(PageParameters parameters, Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters,boolean isEditingNewRecord, boolean isSaveResponse) {
        super(parameters, backlinkPage, backlinkParameters);
        isEditing = true;
        ManualRecord record = service.loadManualRecord(getManualRecordId());
        Form<ManualRecord> form = new AddManualRecordForm("form", model(from(record)), backlinkParameters, isEditingNewRecord, isSaveResponse);
        addComponents(form);
    }


    private void addComponents(Form<ManualRecord> form) {
        add(createBacklink("cancelButton1"));
        form.add(createBacklink("cancelButton2"));

        if(isEditing){
            add(new Label("pageTitle", new ResourceModel("page.title.editmode")));
        }
        else{
            add(new Label("pageTitle", new ResourceModel("page.title.createmode")));
        }

        add(form);
    }

    /**
     * Creates a valid PageParameters object to pass into the constructor of this page class.
     *
     * @param recordId id of the record to edit.
     * @return a valid PageParameters object.
     */
    public static PageParameters createParameters(long recordId) {
        PageParameters parameters = new PageParameters();
        parameters.add("RecordId", recordId);
        return parameters;
    }

    private long getManualRecordId() {
        StringValue value = getPageParameters().get("RecordId");
        if (value == null || value.isEmpty() || value.isNull()) {
            return 0L;
        } else {
            return value.toLong();
        }
    }
}
