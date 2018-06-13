package org.wickedsource.budgeteer.web.components.money;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.components.nullmodel.NullsafeModel;

public class MoneyLabel extends Label {

	private boolean prependCurrencySymbol;

	public MoneyLabel(String id, IModel<Money> model) {
		super(id, new NullsafeModel<Money>(model, MoneyUtil.ZERO));
	}

	public MoneyLabel(String id, IModel<Money> model, boolean prependCurrencySymbol) {
		super(id, new NullsafeModel<Money>(model, MoneyUtil.ZERO));
		this.prependCurrencySymbol = prependCurrencySymbol;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Money> IConverter<Money> getConverter(Class<Money> type) {
		return (IConverter<Money>) new MoneyConverter(prependCurrencySymbol);
	}
}
