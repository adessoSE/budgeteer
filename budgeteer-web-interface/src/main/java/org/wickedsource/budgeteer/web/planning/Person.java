package org.wickedsource.budgeteer.web.planning;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.money.Money;

public class Person {

	private final String name;

	private final Percent availability;

	private final Money dailyRate;

	private final List<Allocation> allocations = new ArrayList<>();

	private final List<TimePeriod> absences = new ArrayList<>();

	private boolean isOverloaded;

	private Percent workload;

	private BigDecimal allocatedWorkDays;

	private BigDecimal availableWorkDays;

	public Person(String name, Percent availability, Money dailyRate) {
		this.name = name;
		this.availability = availability;
		this.dailyRate = dailyRate;
	}

	public String getName() {
		return name;
	}

	public Percent getAvailability() {
		return availability;
	}

	public Money getDailyRate() {
		return dailyRate;
	}

	public List<Allocation> getAllocations() {
		return allocations;
	}

	public boolean isOverloaded() {
		return isOverloaded;
	}

	public List<TimePeriod> getAbsences() {
		return absences;
	}

	public void addAbsence(TimePeriod absence) {
		this.absences.add(absence);
	}

	public void recalculate(Configuration config) {
		workload = Percent.ZERO;
		BigDecimal totalAvailableWorkDays = BigDecimal.valueOf(config.getCalendar().getNumberOfWorkingDays(absences));

		for (Allocation allocation : allocations) {
			workload = workload.add(allocation.getWorkload());
		}

		allocatedWorkDays = workload.of(totalAvailableWorkDays);
		availableWorkDays = totalAvailableWorkDays.subtract(allocatedWorkDays);

		if(workload.greaterThan(availability)){
			this.isOverloaded = true;
		}else{
			this.isOverloaded = false;
		}
	}

	public BigDecimal getAvailableWorkDays() {
		return availableWorkDays;
	}

	public void setAvailableWorkDays(BigDecimal availableWorkDays) {
		this.availableWorkDays = availableWorkDays;
	}

	public Percent getWorkload() {
		return workload;
	}
}
