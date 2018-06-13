package org.wickedsource.budgeteer.service.statistics;

import java.util.*;

import javax.transaction.Transactional;

import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.contract.ContractStatisticBean;
import org.wickedsource.budgeteer.persistence.invoice.InvoiceRepository;
import org.wickedsource.budgeteer.persistence.record.*;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.web.pages.contract.details.contractDetailChart.ContractDetailBudgetChart;

@Service
@Transactional
public class StatisticsService {

	private Random random = new Random();

	@Autowired private WorkRecordRepository workRecordRepository;

	@Autowired private PlanRecordRepository planRecordRepository;

	@Autowired private DateUtil dateUtil;

	@Autowired private ShareBeanToShareMapper shareBeanToShareMapper;

	@Autowired private ContractRepository contractRepository;

	@Autowired private InvoiceRepository invoiceRepository;

	/**
	 * Returns the budget burned in each of the last numberOfWeeks weeks. All of the project's budgets
	 * are aggregated.
	 *
	 * @param projectId ID of the project whose budgets to consider
	 * @param numberOfWeeks the number of weeks to look back into the past
	 * @return list of values each being the monetary value of the budget burned in one week. The last
	 *     entry belongs to the current week. A week is considered to start on Monday and end on
	 *     Sunday.
	 */
	public List<Money> getWeeklyBudgetBurnedForProject(long projectId, int numberOfWeeks) {
		Date startDate = dateUtil.weeksAgo(numberOfWeeks);
		List<WeeklyAggregatedRecordBean> weeklyBeans =
				workRecordRepository.aggregateByWeekForProject(projectId, startDate);
		return fillInMissingWeeks(numberOfWeeks, weeklyBeans);
	}

	/**
	 * Returns the budget planned in each of the last numberOfWeeks weeks. All of the project's
	 * budgets are aggregated.
	 *
	 * @param projectId ID of the project whose budgets to consider
	 * @param numberOfWeeks the number of weeks to look back into the past
	 * @return list of values each being the monetary value of the budget planned in one week. The
	 *     last entry belongs to the current week. A week is considered to start on Monday and end on
	 *     Sunday.
	 */
	public List<Money> getWeeklyBudgetPlannedForProject(long projectId, int numberOfWeeks) {
		Date startDate = dateUtil.weeksAgo(numberOfWeeks);
		List<WeeklyAggregatedRecordBean> weeklyBeans =
				planRecordRepository.aggregateByWeekForProject(projectId, startDate);
		return fillInMissingWeeks(numberOfWeeks, weeklyBeans);
	}

	/**
	 * Returns the budget planned in each of the last numberOfWeeks weeks. All of the person's budgets
	 * are aggregated.
	 *
	 * @param personId ID of the person whose budgets to consider
	 * @param numberOfWeeks the number of weeks to look back into the past
	 * @return list of values each being the monetary value of the budget burned in one week. The last
	 *     entry belongs to the current week. A week is considered to start on Monday and end on
	 *     Sunday.
	 */
	public List<Money> getWeeklyBudgetBurnedForPerson(long personId, int numberOfWeeks) {
		Date startDate = dateUtil.weeksAgo(numberOfWeeks);
		List<WeeklyAggregatedRecordBean> weeklyBeans =
				workRecordRepository.aggregateByWeekForPerson(personId, startDate);
		return fillInMissingWeeks(numberOfWeeks, weeklyBeans);
	}

	/**
	 * Returns the budget planned in each of the last numberOfWeeks weeks. All of the person's budgets
	 * are aggregated.
	 *
	 * @param personId ID of the person whose budgets to consider
	 * @param numberOfWeeks the number of weeks to look back into the past
	 * @return list of values each being the monetary value of the budget planned in one week. The
	 *     last entry belongs to the current week. A week is considered to start on Monday and end on
	 *     Sunday.
	 */
	public List<Money> getWeeklyBudgetPlannedForPerson(long personId, int numberOfWeeks) {
		Date startDate = dateUtil.weeksAgo(numberOfWeeks);
		List<WeeklyAggregatedRecordBean> weeklyBeans =
				planRecordRepository.aggregateByWeekForPerson(personId, startDate);
		return fillInMissingWeeks(numberOfWeeks, weeklyBeans);
	}

	private List<Money> fillInMissingWeeks(
			int numberOfWeeks, List<WeeklyAggregatedRecordBean> weeklyBeans) {
		Date startDate = dateUtil.weeksAgo(numberOfWeeks);
		List<Money> resultList = new ArrayList<Money>();

		// adding values to result list and adding zero-values for weeks that are not included in the
		// query result
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		for (int i = 0; i < numberOfWeeks; i++) {
			WeeklyAggregatedRecordBean weekBean =
					getBeanForWeek(c.get(Calendar.YEAR), c.get(Calendar.WEEK_OF_YEAR), weeklyBeans);
			if (weekBean == null) {
				resultList.add(MoneyUtil.createMoneyFromCents(0l));
			} else {
				resultList.add(MoneyUtil.createMoneyFromCents(weekBean.getValueInCents()));
			}
			c.add(Calendar.WEEK_OF_YEAR, 1);
		}

		return resultList;
	}

	private List<Money> fillInMissingMonths(
			int numberOfMonths, List<MonthlyAggregatedRecordBean> monthlyBeans) {
		Date startDate = dateUtil.monthsAgo(numberOfMonths);
		List<Money> resultList = new ArrayList<Money>();

		// adding values to result list and adding zero-values for weeks that are not included in the
		// query result
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		for (int i = 0; i < numberOfMonths; i++) {
			MonthlyAggregatedRecordBean weekBean =
					getBeanForMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH), monthlyBeans);
			if (weekBean == null) {
				resultList.add(MoneyUtil.createMoneyFromCents(0l));
			} else {
				resultList.add(MoneyUtil.createMoneyFromCents(weekBean.getValueInCents()));
			}
			c.add(Calendar.MONTH, 1);
		}

		return resultList;
	}

	private WeeklyAggregatedRecordBean getBeanForWeek(
			int year, int week, List<WeeklyAggregatedRecordBean> beans) {
		for (WeeklyAggregatedRecordBean bean : beans) {
			if (bean.getYear() == year && bean.getWeek() == week) {
				return bean;
			}
		}
		return null;
	}

	private MonthlyAggregatedRecordBean getBeanForMonth(
			int year, int month, List<MonthlyAggregatedRecordBean> beans) {
		for (MonthlyAggregatedRecordBean bean : beans) {
			if (bean.getYear() == year && bean.getMonth() == month) {
				return bean;
			}
		}
		return null;
	}

	/**
	 * Returns the average daily rate calculated for each of the last numberOfDays days. The average
	 * is calculated over all of the user's budgets.
	 *
	 * @param projectId ID of the project whose budgets to consider
	 * @param numberOfDays the number of days to look back into the past
	 * @return list of values each being the monetary value of the average daily rate that was earned
	 *     for all people working on the user's budgets.
	 */
	public List<Money> getAvgDailyRateForPreviousDays(long projectId, int numberOfDays) {
		Date startDate = dateUtil.daysAgo(numberOfDays);
		List<DailyAverageRateBean> rates =
				workRecordRepository.getAverageDailyRatesPerDay(projectId, startDate);
		List<Money> resultList = new ArrayList<Money>();

		// adding values to result list and adding zeros for days that are not in the query result
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		for (int i = 0; i < numberOfDays; i++) {
			DailyAverageRateBean dayBean =
					getBeanForDay(
							c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), rates);
			if (dayBean == null) {
				resultList.add(MoneyUtil.createMoneyFromCents(0l));
			} else {
				resultList.add(dayBean.getRate());
			}
			c.add(Calendar.DAY_OF_YEAR, 1);
		}

		return resultList;
	}

	private DailyAverageRateBean getBeanForDay(
			int year, int month, int day, List<DailyAverageRateBean> beans) {
		for (DailyAverageRateBean bean : beans) {
			if (bean.getYear() == year && bean.getMonth() == month && bean.getDay() == day) {
				return bean;
			}
		}
		return null;
	}

	/**
	 * Returns the budget burned in monetary value for all budgets a person has worked on.
	 *
	 * @param personId id of the person whose budget share to calculate
	 * @return list of Share objects
	 */
	public List<Share> getBudgetDistribution(long personId) {
		List<ShareBean> shares = workRecordRepository.getBudgetShareForPerson(personId);
		return shareBeanToShareMapper.map(shares);
	}

	/**
	 * Returns the share of all people that have worked on the given budget in monetary value.
	 *
	 * @param budgetId id of the budget whose person share to calculate
	 * @return list of Share objects
	 */
	public List<Share> getPeopleDistribution(long budgetId) {
		List<ShareBean> shares = workRecordRepository.getPersonShareForBudget(budgetId);
		return shareBeanToShareMapper.map(shares);
	}

	/**
	 * Returns the actual and target budget values for the given person from the last numberOfWeeks
	 * weeks.
	 *
	 * @param personId ID of the person whose data to load.
	 * @param numberOfWeeks the number of weeks to go back into the past.
	 * @return the week statistics for the last numberOfWeeks weeks
	 */
	public TargetAndActual getWeekStatsForPerson(long personId, int numberOfWeeks) {
		Date startDate = dateUtil.weeksAgo(numberOfWeeks);
		List<WeeklyAggregatedRecordWithTitleBean> burnedStats =
				workRecordRepository.aggregateByWeekAndBudgetForPerson(personId, startDate);
		List<WeeklyAggregatedRecordBean> plannedStats =
				planRecordRepository.aggregateByWeekForPerson(personId, startDate);

		TargetAndActual targetAndActual = new TargetAndActual();

		MoneySeries targetSeries = new MoneySeries();
		targetSeries.setName("Target");
		targetSeries.setValues(fillInMissingWeeks(numberOfWeeks, plannedStats));
		targetAndActual.setTargetSeries(targetSeries);

		fillInMissingWeeks(numberOfWeeks, burnedStats, targetAndActual);

		return targetAndActual;
	}

	private void fillInMissingWeeks(
			int numberOfWeeks,
			List<WeeklyAggregatedRecordWithTitleBean> burnedStats,
			TargetAndActual targetAndActual) {
		Date startDate = dateUtil.weeksAgo(numberOfWeeks);
		Set<String> titles = getAllTitlesWeekly(burnedStats);
		Calendar c = Calendar.getInstance();
		for (String title : titles) {
			c.setTime(startDate);
			MoneySeries titeledSeries = new MoneySeries();
			titeledSeries.setName(title);
			for (int i = 0; i < numberOfWeeks; i++) {
				WeeklyAggregatedRecordWithTitleBean bean =
						getBeanForWeekAndTitle(
								c.get(Calendar.YEAR), c.get(Calendar.WEEK_OF_YEAR), title, burnedStats);
				if (bean == null) {
					titeledSeries.add(MoneyUtil.createMoneyFromCents(0l));
				} else {
					titeledSeries.add(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
				}
				c.add(Calendar.WEEK_OF_YEAR, 1);
			}
			targetAndActual.getActualSeries().add(titeledSeries);
		}
	}

	private void fillInMissingMonths(
			int numberOfMonths,
			List<MonthlyAggregatedRecordWithTitleBean> burnedStats,
			TargetAndActual targetAndActual) {
		Date startDate = dateUtil.monthsAgo(numberOfMonths);
		Set<String> titles = getAllTitlesMonthly(burnedStats);
		Calendar c = Calendar.getInstance();
		for (String title : titles) {
			c.setTime(startDate);
			MoneySeries titledSeries = new MoneySeries();
			titledSeries.setName(title);
			for (int i = 0; i < numberOfMonths; i++) {
				MonthlyAggregatedRecordWithTitleBean bean =
						getBeanForMonthAndTitle(
								c.get(Calendar.YEAR), c.get(Calendar.MONTH), title, burnedStats);
				if (bean == null) {
					titledSeries.add(MoneyUtil.createMoneyFromCents(0l));
				} else {
					titledSeries.add(MoneyUtil.createMoneyFromCents(bean.getValueInCents()));
				}
				c.add(Calendar.MONTH, 1);
			}
			targetAndActual.getActualSeries().add(titledSeries);
		}
	}

	private Set<String> getAllTitlesWeekly(List<WeeklyAggregatedRecordWithTitleBean> beans) {
		Set<String> budgetNames = new HashSet<String>();
		for (WeeklyAggregatedRecordWithTitleBean bean : beans) {
			budgetNames.add(bean.getTitle());
		}
		return budgetNames;
	}

	private Set<String> getAllTitlesMonthly(List<MonthlyAggregatedRecordWithTitleBean> beans) {
		Set<String> budgetNames = new HashSet<String>();
		for (MonthlyAggregatedRecordWithTitleBean bean : beans) {
			budgetNames.add(bean.getTitle());
		}
		return budgetNames;
	}

	private WeeklyAggregatedRecordWithTitleBean getBeanForWeekAndTitle(
			int year, int week, String title, List<WeeklyAggregatedRecordWithTitleBean> beans) {
		for (WeeklyAggregatedRecordWithTitleBean bean : beans) {
			if (bean.getYear() == year && bean.getWeek() == week && bean.getTitle().equals(title)) {
				return bean;
			}
		}
		return null;
	}

	private MonthlyAggregatedRecordWithTitleBean getBeanForMonthAndTitle(
			int year, int month, String title, List<MonthlyAggregatedRecordWithTitleBean> beans) {
		for (MonthlyAggregatedRecordWithTitleBean bean : beans) {
			if (bean.getYear() == year && bean.getMonth() == month && bean.getTitle().equals(title)) {
				return bean;
			}
		}
		return null;
	}

	/**
	 * Returns the actual and target budget values for the given person from the last numberOfMonths
	 * months.
	 *
	 * @param personId ID of the person whose data to load.
	 * @param numberOfMonths the number of months to go back into the past.
	 * @return the month statistics for the last numberOfMonth months
	 */
	public TargetAndActual getMonthStatsForPerson(long personId, int numberOfMonths) {
		Date startDate = dateUtil.monthsAgo(numberOfMonths);
		List<MonthlyAggregatedRecordWithTitleBean> burnedStats =
				workRecordRepository.aggregateByMonthAndBudgetForPerson(personId, startDate);
		List<MonthlyAggregatedRecordBean> plannedStats =
				planRecordRepository.aggregateByMonthForPerson(personId, startDate);

		TargetAndActual targetAndActual = new TargetAndActual();

		MoneySeries targetSeries = new MoneySeries();
		targetSeries.setName("Target");
		targetSeries.setValues(fillInMissingMonths(numberOfMonths, plannedStats));
		targetAndActual.setTargetSeries(targetSeries);

		fillInMissingMonths(numberOfMonths, burnedStats, targetAndActual);

		return targetAndActual;
	}

	/**
	 * Returns the actual and target budget values for a set of given budgets aggregated by week.
	 *
	 * @param budgetFilter The filter that identified the budgets whose data to load.
	 * @param numberOfWeeks the number of weeks to go back into the past.
	 * @return the week statistics for the last numberOfWeeks weeks
	 */
	public TargetAndActual getWeekStatsForBudgets(BudgetTagFilter budgetFilter, int numberOfWeeks) {
		Date startDate = dateUtil.weeksAgo(numberOfWeeks);
		List<WeeklyAggregatedRecordWithTitleBean> burnedStats =
				new ArrayList<WeeklyAggregatedRecordWithTitleBean>();
		List<WeeklyAggregatedRecordBean> plannedStats = new ArrayList<WeeklyAggregatedRecordBean>();
		if (budgetFilter.getSelectedTags().isEmpty()) {
			burnedStats =
					workRecordRepository.aggregateByWeekAndPersonForBudgets(
							budgetFilter.getProjectId(), startDate);
			plannedStats =
					planRecordRepository.aggregateByWeekForBudgets(budgetFilter.getProjectId(), startDate);
		} else {
			burnedStats =
					workRecordRepository.aggregateByWeekAndPersonForBudgets(
							budgetFilter.getProjectId(), budgetFilter.getSelectedTags(), startDate);
			plannedStats =
					planRecordRepository.aggregateByWeekForBudgets(
							budgetFilter.getProjectId(), budgetFilter.getSelectedTags(), startDate);
		}

		TargetAndActual targetAndActual = new TargetAndActual();

		MoneySeries targetSeries = new MoneySeries();
		targetSeries.setName("Target");
		targetSeries.setValues(fillInMissingWeeks(numberOfWeeks, plannedStats));
		targetAndActual.setTargetSeries(targetSeries);

		fillInMissingWeeks(numberOfWeeks, burnedStats, targetAndActual);

		return targetAndActual;
	}

	/**
	 * Returns the actual and target budget values for a set of given budgets from the last
	 * numberOfMonths months.
	 *
	 * @param budgetFilter The filter that identified the budgets whose data to load.
	 * @param numberOfMonths the number of months to go back into the past.
	 * @return the month statistics for the last numberOfMonths months
	 */
	public TargetAndActual getMonthStatsForBudgets(BudgetTagFilter budgetFilter, int numberOfMonths) {
		Date startDate = dateUtil.monthsAgo(numberOfMonths);
		List<MonthlyAggregatedRecordWithTitleBean> burnedStats =
				new ArrayList<MonthlyAggregatedRecordWithTitleBean>();
		List<MonthlyAggregatedRecordBean> plannedStats = new ArrayList<MonthlyAggregatedRecordBean>();
		if (budgetFilter.getSelectedTags().isEmpty()) {
			// aggregate all budgets
			burnedStats =
					workRecordRepository.aggregateByMonthAndPersonForBudgets(
							budgetFilter.getProjectId(), startDate);
			plannedStats =
					planRecordRepository.aggregateByMonthForBudgets(budgetFilter.getProjectId(), startDate);
		} else {
			// aggregate only budgets with the selected tags
			burnedStats =
					workRecordRepository.aggregateByMonthAndPersonForBudgets(
							budgetFilter.getProjectId(), budgetFilter.getSelectedTags(), startDate);
			plannedStats =
					planRecordRepository.aggregateByMonthForBudgets(
							budgetFilter.getProjectId(), budgetFilter.getSelectedTags(), startDate);
		}

		TargetAndActual targetAndActual = new TargetAndActual();

		MoneySeries targetSeries = new MoneySeries();
		targetSeries.setName("Target");
		targetSeries.setValues(fillInMissingMonths(numberOfMonths, plannedStats));
		targetAndActual.setTargetSeries(targetSeries);

		fillInMissingMonths(numberOfMonths, burnedStats, targetAndActual);

		return targetAndActual;
	}

	/**
	 * Returns the actual and target budget values for a single budget aggregated by week.
	 *
	 * @param budgetId ID of the budget whose data to load
	 * @param numberOfWeeks the number of weeks to go back into the past.
	 * @return the week statistics for the last numberOfWeeks weeks
	 */
	public TargetAndActual getWeekStatsForBudget(long budgetId, int numberOfWeeks) {
		Date startDate = dateUtil.weeksAgo(numberOfWeeks);
		List<WeeklyAggregatedRecordWithTitleBean> burnedStats =
				workRecordRepository.aggregateByWeekAndPersonForBudget(budgetId, startDate);
		List<WeeklyAggregatedRecordBean> plannedStats =
				planRecordRepository.aggregateByWeekForBudget(budgetId, startDate);

		TargetAndActual targetAndActual = new TargetAndActual();

		MoneySeries targetSeries = new MoneySeries();
		targetSeries.setName("Target");
		targetSeries.setValues(fillInMissingWeeks(numberOfWeeks, plannedStats));
		targetAndActual.setTargetSeries(targetSeries);

		fillInMissingWeeks(numberOfWeeks, burnedStats, targetAndActual);

		return targetAndActual;
	}

	/**
	 * Returns the actual and target budget values for a single budget from the last numberOfMonths
	 * months.
	 *
	 * @param budgetId ID of the budget whose data to load
	 * @param numberOfMonths the number of months to go back into the past.
	 * @return the month statistics for the last numberOfMonths months
	 */
	public TargetAndActual getMonthStatsForBudget(long budgetId, int numberOfMonths) {
		Date startDate = dateUtil.monthsAgo(numberOfMonths);
		List<MonthlyAggregatedRecordWithTitleBean> burnedStats =
				workRecordRepository.aggregateByMonthAndPersonForBudget(budgetId, startDate);
		List<MonthlyAggregatedRecordBean> plannedStats =
				planRecordRepository.aggregateByMonthForBudget(budgetId, startDate);

		TargetAndActual targetAndActual = new TargetAndActual();

		MoneySeries targetSeries = new MoneySeries();
		targetSeries.setName("Target");
		targetSeries.setValues(fillInMissingMonths(numberOfMonths, plannedStats));
		targetAndActual.setTargetSeries(targetSeries);

		fillInMissingMonths(numberOfMonths, burnedStats, targetAndActual);

		return targetAndActual;
	}

	public List<ContractStatisticBean> getMonthlyAggregatedStatisticsForContract(
			long contractId, int numberOfMonths) {
		List<ContractStatisticBean> result = new LinkedList<ContractStatisticBean>();
		Date startDate = dateUtil.monthsAgo(numberOfMonths);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());
		while (cal.before(currentDate)) {
			ContractStatisticBean bean =
					contractRepository.getContractStatisticAggregatedByMonthAndYear(
							contractId, cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
			result.add(bean);
			cal.add(Calendar.MONTH, 1);
		}
		return result;
	}

	public List<ContractStatisticBean> getMonthlyStatisticsForContract(
			long contractId, Date startDate) {
		List<ContractStatisticBean> result = new LinkedList<ContractStatisticBean>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		Calendar currentDate = Calendar.getInstance();
		currentDate.setTime(new Date());
		while (cal.before(currentDate)) {
			ContractStatisticBean bean =
					contractRepository.getContractStatisticByMonthAndYear(
							contractId, cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
			result.add(bean);
			cal.add(Calendar.MONTH, 1);
		}
		return result;
	}

	public ContractDetailBudgetChart getMonthlyBudgetBurnedForContract(
			long contractId, int numberOfMonths) {
		ContractDetailBudgetChart result = new ContractDetailBudgetChart();
		List<ContractStatisticBean> values =
				getMonthlyAggregatedStatisticsForContract(contractId, numberOfMonths);
		for (ContractStatisticBean bean : values) {
			result
					.getRemainingTotalBudget()
					.add(MoneyUtil.createMoneyFromCents(bean.getRemainingContractBudget()));
			result.getBurnedMoneyAllBudgets().add(MoneyUtil.createMoneyFromCents(bean.getSpentBudget()));
			result.getBurnedMoneyInvoice().add(MoneyUtil.createMoneyFromCents(bean.getInvoicedBudget()));
		}
		return result;
	}

	protected void fillMissingMonths(
			int numberOfMonths, List<MonthlyAggregatedRecordBean> bean, List<Money> resultList) {
		fillMissingMonths(numberOfMonths, bean, resultList, null);
	}

	protected void fillMissingMonths(
			int numberOfMonths,
			List<MonthlyAggregatedRecordBean> bean,
			List<Money> resultList,
			Money emptyValue) {
		Date startDate = dateUtil.monthsAgo(numberOfMonths);
		Calendar c = Calendar.getInstance();
		c.setTime(startDate);
		for (int i = 0; i < numberOfMonths; i++) {
			boolean monthFound = false;
			for (MonthlyAggregatedRecordBean record : bean) {
				if (record.getMonth() == c.get(Calendar.MONTH)
						&& record.getYear() == c.get(Calendar.YEAR)) {
					resultList.add(MoneyUtil.createMoneyFromCents(record.getValueInCents()));
					monthFound = true;
					break;
				}
			}
			if (!monthFound) {
				if (emptyValue == null) {
					resultList.add(MoneyUtil.createMoneyFromCents(0));
				} else {
					resultList.add(emptyValue);
				}
			}
			c.add(Calendar.MONTH, 1);
		}
	}
}
