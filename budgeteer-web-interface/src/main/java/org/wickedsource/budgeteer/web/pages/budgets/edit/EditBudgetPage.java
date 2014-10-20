package org.wickedsource.budgeteer.web.pages.budgets.edit;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.budget.EditBudgetData;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPage;
import org.wickedsource.budgeteer.web.pages.budgets.edit.form.EditBudgetForm;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount({"budgets/edit/${id}"})
public class EditBudgetPage extends DialogPage {

    @SpringBean
    private BudgetService service;

    /**
     * Use this constructor to create a page with a form to edit an existing user.
     *
     * @param parameters page parameters containing the id of the user to edit.
     */
    public EditBudgetPage(PageParameters parameters, Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(parameters, backlinkPage, backlinkParameters);

        EditBudgetData budgetData = service.loadBudgetToEdit(getBudgetId());
        Form<EditBudgetData> form = new EditBudgetForm("form", model(from(budgetData)));
        add(createBacklink("cancelButton1"));
        form.add(createBacklink("cancelButton2"));
        add(form);
    }

    /**
     * Creates a valid PageParameters object to pass into the constructor of this page class.
     *
     * @param budgetId id of the budget whose details to edit.
     * @return a valid PageParameters object.
     */
    public static PageParameters createParameters(long budgetId) {
        PageParameters parameters = new PageParameters();
        parameters.add("id", budgetId);
        return parameters;
    }

    private long getBudgetId() {
        StringValue value = getPageParameters().get("id");
        if (value == null || value.isEmpty() || value.isNull()) {
            return 0l;
        } else {
            return value.toLong();
        }
    }

}
