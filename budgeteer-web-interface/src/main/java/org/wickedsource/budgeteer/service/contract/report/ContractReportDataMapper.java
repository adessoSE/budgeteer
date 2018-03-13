package org.wickedsource.budgeteer.service.contract.report;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractFieldEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.contract.ContractStatisticBean;
import org.wickedsource.budgeteer.service.AbstractMapper;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;

@Component
public class ContractReportDataMapper extends AbstractMapper<ContractEntity,ContractReportData>{

	@Autowired
	private ContractRepository contractRepository;
	
	@Override
	public ContractReportData map(ContractEntity contract) {
		LocalDate now = LocalDate.now();
		ContractStatisticBean statistics = contractRepository.getContractStatisticByMonthAndYear(contract.getId(), now.getMonthValue(), now.getYear());
		DateRange dateRange = new DateRange(contract.getStartDate(), Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		ContractReportData report = new ContractReportData();
		report.setContract(contract.getName());
		report.setContractId(contract.getInternalNumber());

        Map<String, DynamicAttributeField> contractAttributes = new HashMap<String, DynamicAttributeField>();
        for(ContractFieldEntity fieldEntity : contract.getContractFields()){
            contractAttributes.put(fieldEntity.getField().getFieldName(), new DynamicAttributeField(fieldEntity.getField().getFieldName(), fieldEntity.getValue()));
        }
		report.setAttributes(new ArrayList<DynamicAttributeField>(contractAttributes.values()));
		report.setId(contract.getId());
		report.setTaxRate(contract.getTaxRate()/100);
		report.setFrom(dateRange.getStartDate());
		report.setUntil(dateRange.getEndDate());
		report.setBudgetSpent_net(MoneyUtil.createMoneyFromCents(contractRepository.getSpentBudgetByContractId(contract.getId()).longValue()).getAmount().doubleValue());
		report.setBudgetLeft_net(contract.getBudget().getAmount().doubleValue() - report.getBudgetSpent_net());
		report.setBudgetTotal_net(contract.getBudget().getAmount().doubleValue());

		double taxCoefficient = 1.0 + contract.getTaxRate()/100;
		report.setBudgetSpent_gross(report.getBudgetSpent_net() * taxCoefficient);
		report.setBudgetLeft_gross(report.getBudgetLeft_net() * taxCoefficient);
		report.setBudgetTotal_gross(report.getBudgetTotal_net() * taxCoefficient);
		
		report.setProgress(statistics.getProgress());
		return report;
	}

    public List<ContractReportData> map(List<ContractEntity> entityList){
        List<ContractReportData> result = new LinkedList<ContractReportData>();
        for(ContractEntity entity : entityList){
            result.add(map(entity));
        }
        return result;
    }


}
