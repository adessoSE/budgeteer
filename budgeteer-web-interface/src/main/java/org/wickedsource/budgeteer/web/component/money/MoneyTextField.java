package org.wickedsource.budgeteer.web.component.money;


import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.joda.money.Money;

public class MoneyTextField extends TextField<Money> {

    public MoneyTextField(String id, IModel<Money> model) {
        super(id, model);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Money> IConverter<Money> getConverter(Class<Money> type) {
        return (IConverter<Money>) new MoneyConverter();
    }

    @Override
    protected String getInputType() {
        return "number";
    }
}
