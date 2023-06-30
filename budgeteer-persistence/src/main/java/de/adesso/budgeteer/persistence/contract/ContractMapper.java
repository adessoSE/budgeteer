package de.adesso.budgeteer.persistence.contract;

import de.adesso.budgeteer.core.common.Attachment;
import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.domain.ContractSummary;
import de.adesso.budgeteer.persistence.budget.BudgetEntity;
import de.adesso.budgeteer.persistence.project.ProjectContractField;
import de.adesso.budgeteer.persistence.record.WorkRecordEntity;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.stereotype.Component;

@Component
public class ContractMapper {
  public Contract mapToDomain(ContractEntity contractEntity) {
    var budgetSpent =
        contractEntity.getBudgets().stream()
            .map(BudgetEntity::getWorkRecords)
            .flatMap(List::stream)
            .map(WorkRecordEntity::getActualRate)
            .reduce(Money.of(CurrencyUnit.EUR, 0), Money::plus);
    var budgetLeft = contractEntity.getBudget().minus(budgetSpent);

    var contractAttributes =
        contractEntity.getProject().getContractFields().stream()
            .collect(
                Collectors.toMap(
                    ProjectContractField::getFieldName, a -> "", (a, b) -> a, LinkedHashMap::new));
    contractEntity
        .getContractFields()
        .forEach(
            field -> contractAttributes.put(field.getField().getFieldName(), field.getValue()));
    return new Contract(
        contractEntity.getId(),
        contractEntity.getProject().getId(),
        contractEntity.getInternalNumber(),
        contractEntity.getName(),
        mapType(contractEntity.getType()),
        contractEntity.getStartDate(),
        contractEntity.getBudget(),
        budgetSpent,
        budgetLeft,
        contractEntity.getTaxRate(),
        contractAttributes,
        new Attachment(
            contractEntity.getFileName(), contractEntity.getLink(), contractEntity.getFile()));
  }

  public List<Contract> mapToDomain(List<ContractEntity> contractEntities) {
    return contractEntities.stream().map(this::mapToDomain).collect(Collectors.toList());
  }

  public ContractSummary mapToContractSummary(
      ContractEntity contractEntity, LocalDate from, LocalDate until) {
    var budgetBurnedUpToYearAndMonth =
        contractEntity.getBudgets().stream()
            .flatMap(
                budget ->
                    budget.getWorkRecords().stream()
                        .filter(
                            workRecord ->
                                inYearAndMonthRange(
                                    workRecord.getYear(), workRecord.getMonth(), from, until)))
            .map(WorkRecordEntity::getActualRate)
            .reduce(Money.of(CurrencyUnit.EUR, 0), Money::plus);
    var budgetLeftUpToYearAndMonth = contractEntity.getBudget().minus(budgetBurnedUpToYearAndMonth);
    return new ContractSummary(
        this.mapToDomain(contractEntity),
        from,
        until,
        budgetBurnedUpToYearAndMonth,
        budgetLeftUpToYearAndMonth);
  }

  public List<ContractSummary> mapToContractSummaries(
      List<ContractEntity> contractEntities, LocalDate from, LocalDate until) {
    return contractEntities.stream()
        .map(contractEntity -> mapToContractSummary(contractEntity, from, until))
        .collect(Collectors.toList());
  }

  private boolean inYearAndMonthRange(int year, int month, LocalDate from, LocalDate until) {
    if (year > from.getYear() && year < until.getYear()) {
      return true;
    }
    if (year == from.getYear()
        && month >= from.getMonthValue() - 1
        && (year < until.getYear()
            || (year == until.getYear() && month <= until.getMonthValue() - 1))) {
      return true;
    }
    return year == until.getYear()
        && month <= until.getMonthValue() - 1
        && (year > from.getYear() || (year == from.getYear() && month >= from.getMonthValue() - 1));
  }

  private Contract.Type mapType(ContractEntity.ContractType type) {
    if (type == ContractEntity.ContractType.FIXED_PRICE) {
      return Contract.Type.FIXED_PRICE;
    }
    return Contract.Type.TIME_AND_MATERIAL;
  }
}
