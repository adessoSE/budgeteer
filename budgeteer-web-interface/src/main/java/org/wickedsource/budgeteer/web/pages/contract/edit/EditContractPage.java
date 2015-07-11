package org.wickedsource.budgeteer.web.pages.contract.edit;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.contract.edit.form.EditContractForm;
import org.wickedsource.budgeteer.web.pages.contract.overview.ContractOverviewPage;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount({"contracts/edit/${id}", "contracts/edit"})
public class EditContractPage extends DialogPageWithBacklink {

    @SpringBean
    private ContractService service;

    /**
     * Use this constructor to create a page with a form to create a new contract.
     */
    public EditContractPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        Form<ContractBaseData> form = new EditContractForm("form");
        addComponents(form);
    }

    /**
     * Use this constructor to create a page with a form to edit an existing contract.
     *
     * @param parameters page parameters containing the id of the budget to edit.
     */
    public EditContractPage(PageParameters parameters, Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(parameters, backlinkPage, backlinkParameters);
        ContractBaseData contractBaseData = service.getContractById(getContractId());
        EditContractForm form = new EditContractForm("form", model(from(contractBaseData)));
        addComponents(form);
    }

    private void addComponents(Form<ContractBaseData> form) {
        add(createBacklink("cancelButton1"));
        form.add(createBacklink("cancelButton2"));
        add(form);
    }


    private long getContractId() {
        StringValue value = getPageParameters().get("id");
        if (value == null || value.isEmpty() || value.isNull()) {
            return 0l;
        } else {
            return value.toLong();
        }
    }

    public static PageParameters createParameters(long contractId) {
        PageParameters parameters = new PageParameters();
        parameters.add("id", contractId);
        parameters.add("backling", ContractOverviewPage.class);
        return parameters;
    }
}
