package de.adesso.budgeteer.persistence.budget;

import de.adesso.budgeteer.common.money.MoneyUtil;
import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.persistence.record.RecordEntity;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {
  public Budget mapToDomain(BudgetEntity budgetEntity) {
    var spent =
        MoneyUtil.total(
            budgetEntity.getWorkRecords(), RecordEntity::getActualRate, CurrencyUnit.EUR);
    var lastUpdated =
        budgetEntity.getWorkRecords().stream()
            .map(RecordEntity::getDate)
            .max(Date::compareTo)
            .orElse(null);
    var tags =
        budgetEntity.getTags().stream().map(BudgetTagEntity::getTag).collect(Collectors.toList());
    var totalMinutes =
        budgetEntity.getWorkRecords().stream().mapToInt(RecordEntity::getMinutes).sum();
    var totalRate =
        MoneyUtil.total(
            budgetEntity.getWorkRecords(),
            workRecord -> workRecord.getDailyRate().multipliedBy(workRecord.getMinutes()),
            CurrencyUnit.EUR);
    var averageDailyRate =
        totalMinutes == 0
            ? Money.zero(CurrencyUnit.EUR)
            : totalRate.dividedBy(totalMinutes, RoundingMode.HALF_DOWN);
    return new Budget(
        budgetEntity.getId(),
        budgetEntity.getContract() == null ? null : budgetEntity.getContract().getId(),
        budgetEntity.getName(),
        budgetEntity.getContract() == null ? null : budgetEntity.getContract().getName(),
        budgetEntity.getDescription(),
        budgetEntity.getImportKey(),
        budgetEntity.getTotal(),
        spent,
        budgetEntity.getTotal().minus(spent),
        averageDailyRate,
        budgetEntity.getLimit(),
        lastUpdated,
        tags);
  }

  public List<Budget> mapToDomain(List<BudgetEntity> budgetEntities) {
    return budgetEntities.stream().map(this::mapToDomain).collect(Collectors.toList());
  }
}
