package org.wickedsource.budgeteer.persistence.record;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface RecordRepository extends CrudRepository<WorkRecordEntity, Long> {

    /**
     * Aggregates the monetary value of all work records in the given budget.
     *
     * @param budgetId ID of the budget whose spending to aggregate.
     * @return aggregated monetary value of the spent budget in cents.
     */
    @Query("select sum(record.spentMinutes * record.dailyRate) / 60 / 8 from WorkRecordEntity record where record.budget.id = :budgetId")
    public double getSpentBudget(@Param("budgetId") long budgetId);

    /**
     * Calculates the monetary value of the average daily rate in the given budget.
     *
     * @param budgetId ID of the budget whose average daily rate to calculate
     * @return monetary value of the average daily rate in cents.
     */
    @Query("select sum(record.dailyRate * record.spentMinutes) / sum(record.spentMinutes) from WorkRecordEntity record where record.budget.id=:budgetId")
    public double getAverageDailyRate(@Param("budgetId") long budgetId);

    /**
     * Aggregates the monetary value of all planned records in the given budget.
     *
     * @param budgetId ID of the budget whose planned spending to aggregate.
     * @return aggregated monetary value of the planned budget in cents.
     */
    @Query("select sum(record.plannedMinutes * record.dailyRate) / 60 / 8 from PlanRecordEntity record where record.budget.id = :budgetId")
    public double getPlannedBudget(@Param("budgetId") long budgetId);

    @Query("select max(record.date) from WorkRecordEntity record where record.budget.id=:budgetId")
    public Date getLatestWordRecordDate(@Param("budgetId") long budgetId);

}
