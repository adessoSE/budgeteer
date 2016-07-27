package org.wickedsource.budgeteer.persistence;

import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MoneyAttributeConverter implements AttributeConverter<Money, Long> {

    @Override
    public Long convertToDatabaseColumn(Money money) {
        return money.getAmountMinorLong();
    }

    @Override
    public Money convertToEntityAttribute(Long cents) {
        return MoneyUtil.createMoneyFromCents(cents == null ? 0 : cents);
    }
}
