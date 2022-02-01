package org.wickedsource.budgeteer.persistence.record;

import de.adesso.budgeteer.common.old.MoneyUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.money.Money;

@Data
@AllArgsConstructor
public class ShareBean {

  private String name;

  private long valueInCents;

  public Money getValue() {
    return MoneyUtil.createMoneyFromCents(valueInCents);
  }
}
