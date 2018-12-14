package de.adesso.budgeteer.web.pages.budgets.overview.table;

import de.adesso.budgeteer.service.budget.BudgetDetailData;
import de.adesso.budgeteer.service.budget.BudgetService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class FilteredBudgetModelByContract extends LoadableDetachableModel<List<BudgetDetailData>> {
    @SpringBean
    private BudgetService service;

    private long contractId;


    public FilteredBudgetModelByContract(long contractId) {
        Injector.get().inject(this);
        this.contractId = contractId;
    }

    @Override
    protected List<BudgetDetailData> load() {
        return service.loadBudgetByContract(contractId);
    }
}
