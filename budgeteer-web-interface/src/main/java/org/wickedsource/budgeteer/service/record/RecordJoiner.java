package org.wickedsource.budgeteer.service.record;

import java.util.*;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;

@Component
public class RecordJoiner {

	/**
	* Joins the given work records and the given plan records since it is not possible to join them in the database with JPA.
	*/
	public List<AggregatedRecord> joinWeekly(List<WeeklyAggregatedRecordBean> workRecords, List<WeeklyAggregatedRecordBean> planRecords) {
		RecordMap recordMap = new RecordMap();
		for (WeeklyAggregatedRecordBean bean : planRecords) {
			AggregatedRecord record = recordMap.getOrCreateRecord(bean);
			record.setBudgetPlanned(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
		}
		for (WeeklyAggregatedRecordBean bean : workRecords) {
			AggregatedRecord record = recordMap.getOrCreateRecord(bean);
			record.setBudgetBurned(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
			record.setHours(bean.getHours());
		}
		List<AggregatedRecord> records = new ArrayList<AggregatedRecord>(recordMap.values());
		Collections.sort(records, new AggregatedRecordComparator());
		return records;
	}

	public List<AggregatedRecord> joinMonthly(List<MonthlyAggregatedRecordBean> workRecords, List<MonthlyAggregatedRecordBean> planRecords) {
		RecordMap recordMap = new RecordMap();
		for (MonthlyAggregatedRecordBean bean : planRecords) {
			AggregatedRecord record = recordMap.getOrCreateRecord(bean);
			record.setBudgetPlanned(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
		}
		for (MonthlyAggregatedRecordBean bean : workRecords) {
			AggregatedRecord record = recordMap.getOrCreateRecord(bean);
			record.setBudgetBurned(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
			record.setHours(bean.getHours());
		}
		List<AggregatedRecord> records = new ArrayList<AggregatedRecord>(recordMap.values());
		Collections.sort(records, new AggregatedRecordComparator());
		return records;
	}


	class RecordMap extends HashMap<String, AggregatedRecord> {

		public AggregatedRecord getOrCreateRecord(WeeklyAggregatedRecordBean recordBean) {
			AggregatedRecord record = get(getKey(recordBean));
			if (record == null) {
				Calendar c = Calendar.getInstance(Locale.GERMAN);
				c.clear();
				c.set(Calendar.YEAR, recordBean.getYear());
				c.set(Calendar.WEEK_OF_YEAR, recordBean.getWeek());
				record = new AggregatedRecord();
				record.setAggregationPeriodStart(c.getTime());
				c.add(Calendar.DAY_OF_YEAR, 6);
				record.setAggregationPeriodEnd(c.getTime());
				record.setAggregationPeriodTitle(String.format("Week #%d", recordBean.getWeek()));
				put(getKey(recordBean), record);
			}
			return record;
		}

		public AggregatedRecord getOrCreateRecord(MonthlyAggregatedRecordBean recordBean) {
			AggregatedRecord record = get(getKey(recordBean));
			if (record == null) {
				Calendar c = Calendar.getInstance(Locale.GERMAN);
				c.clear();
				c.set(Calendar.YEAR, recordBean.getYear());
				c.set(Calendar.MONTH, recordBean.getMonth());
				record = new AggregatedRecord();
				record.setAggregationPeriodStart(c.getTime());
				c.add(Calendar.MONTH, 1);
				c.add(Calendar.DAY_OF_YEAR, -1);
				record.setAggregationPeriodEnd(c.getTime());
				record.setAggregationPeriodTitle(String.format("%d/%02d", recordBean.getYear(), recordBean.getMonth() + 1));
				put(getKey(recordBean), record);
			}
			return record;
		}

		private String getKey(WeeklyAggregatedRecordBean recordBean) {
			return String.format("%d%d", recordBean.getYear(), recordBean.getWeek());
		}

		private String getKey(MonthlyAggregatedRecordBean recordBean) {
			return String.format("%d%d", recordBean.getYear(), recordBean.getMonth());
		}

	}


}
