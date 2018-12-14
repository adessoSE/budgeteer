package de.adesso.budgeteer.web.pages.budgets.overview.table;

import de.adesso.budgeteer.service.budget.BudgetDetailData;
import de.adesso.budgeteer.service.budget.BudgetService;
import de.adesso.budgeteer.service.budget.BudgetTagFilter;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class FilteredBudgetModel extends LoadableDetachableModel<List<BudgetDetailData>> {

    @SpringBean
    private BudgetService service;

    private long projectId;

    private IModel<BudgetTagFilter> filterModel;

    private IModel<Long> remainingFilterModel = null;

    public FilteredBudgetModel(long projectId, IModel<BudgetTagFilter> filterModel) {
        Injector.get().inject(this);
        this.filterModel = filterModel;
        this.projectId = projectId;
    }

    @Override
    protected List<BudgetDetailData> load() {
        if(remainingFilterModel == null) {
            return service.loadBudgetsDetailData(projectId, filterModel.getObject());
        }else{
            return service.loadBudgetsDetailData(projectId, filterModel.getObject(), remainingFilterModel.getObject());
        }
    }

    public void setFilter(IModel<BudgetTagFilter> filterModel) {
        this.filterModel = filterModel;
    }

    public void setRemainingFilterModel(IModel<Long> remainingFilterModel){
        this.remainingFilterModel = remainingFilterModel;
    }
}
