package org.wickedsource.budgeteer.web.pages.contract.overview.table;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;

@Data
public class ContractOverviewTableModel implements Serializable{
	private List<ContractBaseData> contracts = new LinkedList<ContractBaseData>();
	private List<String> footer = new LinkedList<String>();
	private boolean taxRateEnabled;

	public List<String> getHeadline() {
		List<String> result = new LinkedList<String>();
		if(contracts.size() > 0){
			for(DynamicAttributeField attribute : contracts.get(0).getContractAttributes()){
				result.add(attribute.getName());
			}
		}
		return result;
	}

}
