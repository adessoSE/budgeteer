package de.adesso.budgeteer.service.person;

import de.adesso.budgeteer.MoneyUtil;
import de.adesso.budgeteer.persistence.person.PersonDetailDataBean;
import de.adesso.budgeteer.service.AbstractMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonDetailDataMapper extends AbstractMapper<PersonDetailDataBean, PersonDetailData> {

    @Override
    public PersonDetailData map(PersonDetailDataBean sourceObject) {
        PersonDetailData data = new PersonDetailData();
        data.setName(sourceObject.getName());
        if (sourceObject.getAverageDailyRateInCents() != null) {
            data.setAverageDailyRate(MoneyUtil.createMoneyFromCents(sourceObject.getAverageDailyRateInCents()));
        }
        data.setLastBookedDate(sourceObject.getLastBookedDate());
        data.setFirstBookedDate(sourceObject.getFirstBookedDate());
        data.setHoursBooked(sourceObject.getHoursBooked());
        if (sourceObject.getBudgetBurnedInCents() != null) {
            data.setBudgetBurned(MoneyUtil.createMoneyFromCents(sourceObject.getBudgetBurnedInCents()));
        }
        return data;
    }
}
