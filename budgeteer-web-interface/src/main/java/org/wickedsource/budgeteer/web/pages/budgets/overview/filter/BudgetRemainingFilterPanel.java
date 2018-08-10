package org.wickedsource.budgeteer.web.pages.budgets.overview.filter;

import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class BudgetRemainingFilterPanel extends Panel {

    public BudgetRemainingFilterPanel(String id, IModel<Long> filterModel) {
        super(id);
        Form form = new Form("remainingFilterForm") {
            @Override
            protected void onSubmit() {
                send(getPage(), Broadcast.BREADTH, filterModel.getObject());
            }
        };
        form.add(createTextInputField("remainingFilterInput", filterModel));
        add(form);
    }

    private NumberTextField<Long> createTextInputField(String id, IModel<Long> value){
        return new NumberTextField<>(id, value);
    }
}
