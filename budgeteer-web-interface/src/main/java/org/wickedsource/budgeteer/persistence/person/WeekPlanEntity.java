package org.wickedsource.budgeteer.persistence.person;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

public class WeekPlanEntity {

    @Id
    @GeneratedValue
    private long id;

    @OneToMany
    private PersonEntity person;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int week;

    @Column(nullable = false)
    private int minutesPlanned;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getMinutesPlanned() {
        return minutesPlanned;
    }

    public void setMinutesPlanned(int minutesPlanned) {
        this.minutesPlanned = minutesPlanned;
    }
}
