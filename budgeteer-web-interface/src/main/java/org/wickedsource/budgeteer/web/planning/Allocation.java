package org.wickedsource.budgeteer.web.planning;

public class Allocation {

	private final Task task;

	private final Person person;

	private final Percent workload;

	public Allocation(Task task, Person person, Percent workload) {
		this.task = task;
		this.person = person;
		this.workload = workload;
	}

	public Task getTask() {
		return task;
	}

	public Person getPerson() {
		return person;
	}

	public Percent getWorkload() {
		return workload;
	}
}
