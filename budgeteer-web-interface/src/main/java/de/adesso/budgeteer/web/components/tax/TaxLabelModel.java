package de.adesso.budgeteer.web.components.tax;

import de.adesso.budgeteer.web.BudgeteerSession;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class TaxLabelModel extends AbstractReadOnlyModel<String> {

    private String NET_FORMAT = "%s (net)";
    private String GROSS_FORMAT = "%s (gross)";

    private IModel<String> model;

    public TaxLabelModel(String value) {
        this.model = Model.of(value);
    }

    public TaxLabelModel(IModel<String> model) {
        this.model = model;
    }

    @Override
    public String getObject() {
        String value = model.getObject();
        if (BudgeteerSession.get().isTaxEnabled()) {
            return String.format(GROSS_FORMAT, value);
        } else {
            return String.format(NET_FORMAT, value);
        }
    }

    @Override
    public void detach() {
        model.detach();
    }
}
