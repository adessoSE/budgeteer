package de.adesso.budgeteer.persistence;

import de.adesso.budgeteer.common.old.MoneyUtil;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.joda.money.Money;

@Converter(autoApply = true)
public class MoneyAttributeConverter implements AttributeConverter<Money, Long> {

  @Override
  public Long convertToDatabaseColumn(Money money) {
    return money == null ? null : money.getAmountMinorLong();
  }

  @Override
  public Money convertToEntityAttribute(Long cents) {
    return cents == null ? null : MoneyUtil.createMoneyFromCents(cents);
  }
}
