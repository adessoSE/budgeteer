package org.wickedsource.budgeteer.service.person;

import de.adesso.budgeteer.common.old.MoneyUtil;
import de.adesso.budgeteer.persistence.person.PersonBaseDataBean;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component
public class PersonBaseDataMapper extends AbstractMapper<PersonBaseDataBean, PersonBaseData> {

  @Override
  public PersonBaseData map(PersonBaseDataBean sourceObject) {
    PersonBaseData data = new PersonBaseData();
    data.setId(sourceObject.getId());
    if (sourceObject.getAverageDailyRateInCents() != null) {
      data.setAverageDailyRate(
          MoneyUtil.createMoneyFromCents(sourceObject.getAverageDailyRateInCents()));
    }
    data.setLastBooked(sourceObject.getLastBookedDate());
    data.setName(sourceObject.getName());
    data.setDefaultDailyRate(sourceObject.getDefaultDailyRate());
    return data;
  }
}
