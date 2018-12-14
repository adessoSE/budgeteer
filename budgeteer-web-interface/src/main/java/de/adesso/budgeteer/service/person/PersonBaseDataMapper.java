package de.adesso.budgeteer.service.person;

import de.adesso.budgeteer.MoneyUtil;
import de.adesso.budgeteer.persistence.person.PersonBaseDataBean;
import org.springframework.stereotype.Component;
import de.adesso.budgeteer.service.AbstractMapper;

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
