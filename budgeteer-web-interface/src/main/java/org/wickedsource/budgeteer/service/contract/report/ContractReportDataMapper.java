package org.wickedsource.budgeteer.service.contract.report;

import de.adesso.budgeteer.common.old.MoneyUtil;
import de.adesso.budgeteer.persistence.contract.ContractEntity;
import de.adesso.budgeteer.persistence.contract.ContractFieldEntity;
import de.adesso.budgeteer.persistence.contract.ContractRepository;
import de.adesso.budgeteer.persistence.contract.ContractStatisticBean;
import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;

@Component
public class ContractReportDataMapper {

  @Autowired private ContractRepository contractRepository;

  public ContractReportData map(ContractEntity contract, Date endDate) {
    LocalDate end = DateUtil.toLocalDate(endDate);
    ContractStatisticBean statistics =
        contractRepository.getContractStatisticAggregatedByMonthAndYear(
            contract.getId(), end.getMonthValue() - 1, end.getYear());
    DateRange dateRange = new DateRange(DateUtil.toDate(contract.getStartDate()), endDate);

    ContractReportData report = new ContractReportData();
    report.setContract(contract.getName());
    report.setContractId(contract.getInternalNumber());

    Map<String, DynamicAttributeField> contractAttributes =
        new HashMap<String, DynamicAttributeField>();
    for (ContractFieldEntity fieldEntity : contract.getContractFields()) {
      contractAttributes.put(
          fieldEntity.getField().getFieldName(),
          new DynamicAttributeField(fieldEntity.getField().getFieldName(), fieldEntity.getValue()));
    }
    report.setAttributes(new ArrayList<DynamicAttributeField>(contractAttributes.values()));
    report.setId(contract.getId());
    report.setTaxRate(
        contract.getTaxRate() != null ? contract.getTaxRate().doubleValue() / 100 : 0);
    report.setFrom(dateRange.getStartDate());
    report.setUntil(dateRange.getEndDate());
    report.setBudgetSpent_net(
        MoneyUtil.createMoneyFromCents(statistics.getSpentBudget()).getAmount().doubleValue());
    report.setBudgetLeft_net(
        contract.getBudget().getAmount().doubleValue() - report.getBudgetSpent_net());
    report.setBudgetTotal_net(contract.getBudget().getAmount().doubleValue());

    double taxCoefficient = 1.0 + report.getTaxRate();
    report.setBudgetSpent_gross(report.getBudgetSpent_net() * taxCoefficient);
    report.setBudgetLeft_gross(report.getBudgetLeft_net() * taxCoefficient);
    report.setBudgetTotal_gross(report.getBudgetTotal_net() * taxCoefficient);

    report.setProgress(statistics.getProgress());
    return report;
  }

  public List<ContractReportData> map(List<ContractEntity> entityList, Date endDate) {
    List<ContractReportData> result = new LinkedList<ContractReportData>();
    for (ContractEntity entity : entityList) {
      result.add(map(entity, endDate));
    }
    return result;
  }
}
