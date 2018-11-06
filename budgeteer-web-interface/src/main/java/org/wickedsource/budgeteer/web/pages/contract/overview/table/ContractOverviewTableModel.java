package org.wickedsource.budgeteer.web.pages.contract.overview.table;

import lombok.Data;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
public class ContractOverviewTableModel implements Serializable{
    private List<ContractBaseData> contracts = new LinkedList<>();
    private List<String> footer = new LinkedList<>();
    private boolean taxRateEnabled;

    public List<String> getHeadline() {
        List<String> result = new LinkedList<>();
        if(contracts.size() > 0){
            for(DynamicAttributeField attribute : contracts.get(0).getContractAttributes()){
                result.add(attribute.getName());
            }
        }
        return result;
    }

}
