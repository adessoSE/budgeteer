package org.wickedsource.budgeteer.web.planning;

import org.junit.Assert;
import org.junit.Test;
import org.wickedsource.budgeteer.MoneyUtil;

import java.util.*;
import java.util.Calendar;

public class ResourcePlanningAssistantTest {

    @Test
    public void testAllocate() throws Exception {

        Task task1 = new Task("Task 1", MoneyUtil.createMoneyFromCents(10000000));
        Task task2 = new Task("Task 2", MoneyUtil.createMoneyFromCents(5000000));

        Person fullTimePerson = new Person("Felix Fulltime", new Percent(100), MoneyUtil.createMoneyFromCents(50000));
        fullTimePerson.addAbsence(new TimePeriod(getDate(2015, Calendar.JULY, 6), getDate(2015, Calendar.JULY, 10))); // 5 working days missing
        fullTimePerson.addAbsence(new TimePeriod(getDate(2015, Calendar.SEPTEMBER, 4), getDate(2015, Calendar.SEPTEMBER, 7))); // 2 working days missing
        Person partTimePerson = new Person("Paul Parttime", new Percent(50), MoneyUtil.createMoneyFromCents(60000));

        Configuration config = getConfiguration();

        ResourcePlanningAssistant assistant = new ResourcePlanningAssistant(config);

        assistant.allocate(task1, fullTimePerson, new Percent(50));
        assistant.allocate(task1, partTimePerson, new Percent(50));
        assistant.allocate(task2, fullTimePerson, new Percent(10));
        assistant.allocate(task2, partTimePerson, new Percent(10));

        Assert.assertEquals(MoneyUtil.createMoneyFromCents(-4180000), task1.getRestBudget());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(2164000), task2.getRestBudget());
        Assert.assertTrue(partTimePerson.isOverloaded());
        Assert.assertFalse(fullTimePerson.isOverloaded());
        Assert.assertTrue(task1.isOverspent());
        Assert.assertFalse(task2.isOverspent());

        assistant.deallocate(task1, partTimePerson);

        Assert.assertEquals(MoneyUtil.createMoneyFromCents(3650000), task1.getRestBudget());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(2164000), task2.getRestBudget());
        Assert.assertFalse(partTimePerson.isOverloaded());
        Assert.assertFalse(fullTimePerson.isOverloaded());
        Assert.assertFalse(task1.isOverspent());
        Assert.assertFalse(task2.isOverspent());
    }

    private Configuration getConfiguration() {
        Configuration config = new Configuration();
        Date start = getDate(2015, Calendar.JANUARY, 1);
        Date end = getDate(2015, Calendar.DECEMBER, 31);

        config.setCalendar(new DefaultCalendar(start, end));
        return config;
    }

    private Date getDate(int year, int month, int dayOfMonth){
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.YEAR, year);
        cal.set(java.util.Calendar.MONTH, month);
        cal.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
        return cal.getTime();
    }
}