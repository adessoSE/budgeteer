package org.wickedsource.budgeteer.persistence.record;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class RecordEntity {

	@Id @GeneratedValue private long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "PERSON_ID")
	private PersonEntity person;

	@ManyToOne(optional = false)
	@JoinColumn(name = "BUDGET_ID")
	private BudgetEntity budget;

	@Temporal(TemporalType.DATE)
	@Column(name = "RECORD_DATE", nullable = false)
	private Date date;

	@Column(name = "RECORD_YEAR", nullable = false)
	private int year;

	@Column(name = "RECORD_MONTH", nullable = false)
	private int month;

	@Column(name = "RECORD_DAY", nullable = false)
	private int day;

	@Column(name = "RECORD_WEEK", nullable = false)
	private int week;

	@Column(nullable = false)
	private int minutes;

	@Column(nullable = false)
	private Money dailyRate;

	@ManyToOne(optional = false)
	@JoinColumn(name = "IMPORT_ID")
	private ImportEntity importRecord;

	public void setDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		this.date = date;
		this.year = c.get(Calendar.YEAR);
		this.month = c.get(Calendar.MONTH);
		this.day = c.get(Calendar.DAY_OF_MONTH);
		this.week = c.get(Calendar.WEEK_OF_YEAR);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RecordEntity)) return false;

		RecordEntity that = (RecordEntity) o;

		if (day != that.day) return false;
		if (id != that.id) return false;
		if (minutes != that.minutes) return false;
		if (month != that.month) return false;
		if (week != that.week) return false;
		if (year != that.year) return false;
		if (budget != null ? !budget.equals(that.budget) : that.budget != null) return false;
		if (dailyRate != null ? !dailyRate.equals(that.dailyRate) : that.dailyRate != null)
			return false;
		if (date != null ? !date.equals(that.date) : that.date != null) return false;
		if (importRecord != null ? !importRecord.equals(that.importRecord) : that.importRecord != null)
			return false;
		if (person != null ? !person.equals(that.person) : that.person != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (person != null ? person.hashCode() : 0);
		result = 31 * result + (budget != null ? budget.hashCode() : 0);
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + year;
		result = 31 * result + month;
		result = 31 * result + day;
		result = 31 * result + week;
		result = 31 * result + minutes;
		result = 31 * result + (dailyRate != null ? dailyRate.hashCode() : 0);
		result = 31 * result + (importRecord != null ? importRecord.hashCode() : 0);
		return result;
	}
}
