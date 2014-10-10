package org.wickedsource.budgeteer.service.people;

import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.service.budget.BudgetBaseData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PeopleService {

    /**
     * Returns all people the given user can make use of to manage budgets.
     *
     * @param userId id of the logged in user.
     * @return list of PersonBaseData objects for all people the given user can make use of to manage budgets.
     */
    public List<PersonBaseData> loadPeopleBaseData(long userId) {
        List<PersonBaseData> list = new ArrayList<PersonBaseData>();
        for (int i = 0; i < 20; i++) {
            list.add(createPersonBaseData());
        }
        return list;
    }

    /**
     * Returns detailed data about the person with the given id.
     *
     * @param personId id of the person whose data to load.
     * @return the detailed data of the specified person.
     */
    public PersonDetailData loadPersonDetailData(long personId) {
        PersonDetailData data = new PersonDetailData();
        data.setAverageDailyRate(100.0);
        data.setName("Tom Hombergs");
        data.setBudgetBurned(100000.00);
        data.setFirstBookedDate(new Date());
        data.setHoursBooked(100.0);
        data.setLastBookedDate(new Date());
        return data;
    }

    /**
     * Loads the data of a person together with the daily rates of that person from the database.
     *
     * @param personId ID of the person whose data to load.
     * @return PersonWithRates object
     */
    public PersonWithRates loadPersonWithRates(long personId) {
        PersonWithRates person = new PersonWithRates();
        person.setName("Tom");
        person.setImportKey("Tom");
        person.setRates(new ArrayList<PersonRate>());

        PersonRate rate1 = new PersonRate();
        rate1.setBudget(new BudgetBaseData(1, "Budget 1"));
        rate1.setDateRange(new DateRange(new Date(), new Date()));
        rate1.setRate(550.5);
        person.getRates().add(rate1);

        PersonRate rate2 = new PersonRate();
        rate2.setBudget(new BudgetBaseData(1, "Budget 2"));
        rate2.setDateRange(new DateRange(new Date(), new Date()));
        rate2.setRate(750.5);
        person.getRates().add(rate2);

        return person;
    }

    private PersonBaseData createPersonBaseData() {
        PersonBaseData person = new PersonBaseData();
        person.setId(1);
        person.setAverageDailyRate(1250.54);
        person.setLastBooked(new Date());
        person.setName("Tom Hombergs");
        return person;
    }

}
