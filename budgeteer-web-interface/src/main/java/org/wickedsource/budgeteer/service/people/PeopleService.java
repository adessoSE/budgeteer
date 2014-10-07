package org.wickedsource.budgeteer.service.people;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PeopleService {

    /**
     * Returns all people the given user can make use of to manage budgets.
     *
     * @param userId id of the logged in user.
     * @return list of Person objects for all people the given user can make use of to manage budgets.
     */
    public List<Person> getPeople(long userId) {
        List<Person> list = new ArrayList<Person>();
        for (int i = 0; i < 20; i++) {
            list.add(createPerson());
        }
        return list;
    }

    private Person createPerson() {
        Person person = new Person();
        person.setId(1);
        person.setAverageDailyRate(1250.54);
        person.setLastBooked(new Date());
        person.setName("Tom Hombergs");
        return person;
    }

}
