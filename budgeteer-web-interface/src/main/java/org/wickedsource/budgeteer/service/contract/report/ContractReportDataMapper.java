package org.wickedsource.budgeteer.service.contract.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractFieldEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.contract.ContractStatisticBean;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class ContractReportDataMapper {

	private final ContractRepository contractRepository;

	@Autowired
	public ContractReportDataMapper(ContractRepository contractRepository) {
		this.contractRepository = contractRepository;
	}

	public ContractReportData map(ContractEntity contract, Date endDate) {
		LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		ContractStatisticBean statistics = contractRepository.getContractStatisticAggregatedByMonthAndYear(contract.getId(), end.getMonthValue()-1, end.getYear());
		DateRange dateRange = new DateRange(contract.getStartDate(), endDate);
		
		ContractReportData report = new ContractReportData();
		report.setContract(contract.getName());
		report.setContractId(contract.getInternalNumber());

        Map<String, DynamicAttributeField> contractAttributes = new HashMap<>();
        for(ContractFieldEntity fieldEntity : contract.getContractFields()){
            contractAttributes.put(fieldEntity.getField().getFieldName(), new DynamicAttributeField(fieldEntity.getField().getFieldName(), fieldEntity.getValue()));
        }
		report.setAttributes(new ArrayList<>(contractAttributes.values()));
		report.setId(contract.getId());
		report.setTaxRate(contract.getTaxRate() != null ? contract.getTaxRate().doubleValue() / 100 : 0);
		report.setFrom(dateRange.getStartDate());
		report.setUntil(dateRange.getEndDate());
		report.setBudgetspentNet(MoneyUtil.createMoneyFromCents(statistics.getSpentBudget()).getAmount().doubleValue());
		report.setBudgetLeftNet(contract.getBudget().getAmount().doubleValue() - report.getBudgetspentNet());
		report.setBudgetTotalNet(contract.getBudget().getAmount().doubleValue());

		double taxCoefficient = 1.0 + report.getTaxRate();
		report.setBudgetSpentGross(report.getBudgetspentNet() * taxCoefficient);
		report.setBudgetLeftGross(report.getBudgetLeftNet() * taxCoefficient);
		report.setBudgetTotalGross(report.getBudgetTotalNet() * taxCoefficient);
		
		report.setProgress(statistics.getProgress());
		return report;
	}

    public List<ContractReportData> map(List<ContractEntity> entityList, Date endDate){
        List<ContractReportData> result = new LinkedList<>();
        for(ContractEntity entity : entityList){
            result.add(map(entity,endDate));
        }
        return result;
    }


}
