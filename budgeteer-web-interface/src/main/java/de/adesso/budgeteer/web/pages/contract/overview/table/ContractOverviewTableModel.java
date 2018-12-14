package de.adesso.budgeteer.web.pages.contract.overview.table;

import de.adesso.budgeteer.service.contract.ContractBaseData;
import de.adesso.budgeteer.service.contract.ContractComparator;
import de.adesso.budgeteer.service.contract.ContractSortingService;
import de.adesso.budgeteer.service.contract.DynamicAttributeField;
import de.adesso.budgeteer.web.BudgeteerSession;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class ContractOverviewTableModel extends LoadableDetachableModel<List<ContractBaseData>> {

    @SpringBean
    private ContractSortingService contractSortingService;

    private List<ContractBaseData> contracts;
    private List<String> footer = new LinkedList<>();
    private boolean taxRateEnabled;

    public ContractOverviewTableModel(){
        Injector.get().inject(this);
    }

    public List<String> getHeadline() {
        contracts = contractSortingService.getSortedContracts(BudgeteerSession.get().getProjectId(), BudgeteerSession.get().getLoggedInUser().getId());
        List<String> result = new LinkedList<>();
        if(!contracts.isEmpty()){
            for(DynamicAttributeField attribute : contracts.get(0).getContractAttributes()){
                result.add(attribute.getName());
            }
        }
        return result;
    }

    @Override
    protected List<ContractBaseData> load() {
        List<ContractBaseData> contractBaseData = contractSortingService.getSortedContracts(BudgeteerSession.get().getProjectId(), BudgeteerSession.get().getLoggedInUser().getId());
        // Sort by sorting index
        contractBaseData.sort(new ContractComparator());

        // Give every contract a unique sequential sorting index
        for (int i = 0; i < contractBaseData.size(); i++) {
            contractBaseData.get(i).setSortingIndex(i);
        }
        return contractBaseData;
    }
}
