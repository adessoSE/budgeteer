package org.wickedsource.budgeteer.service.person;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.person.PersonBaseDataBean;
import org.wickedsource.budgeteer.service.AbstractMapper;

@Component
public class PersonBaseDataMapper extends AbstractMapper<PersonBaseDataBean, PersonBaseData> {

    @Override
    public PersonBaseData map(PersonBaseDataBean sourceObject) {
        PersonBaseData data = new PersonBaseData();
        data.setId(sourceObject.getId());
        if (sourceObject.getAverageDailyRateInCents() != null) {
            data.setAverageDailyRate(MoneyUtil.createMoneyFromCents(sourceObject.getAverageDailyRateInCents()));
        }
        data.setLastBooked(sourceObject.getLastBookedDate());
        data.setName(sourceObject.getName());
        return data;
    }
}
