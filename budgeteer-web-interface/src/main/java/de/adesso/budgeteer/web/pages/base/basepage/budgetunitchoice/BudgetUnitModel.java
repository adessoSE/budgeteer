package de.adesso.budgeteer.web.pages.base.basepage.budgetunitchoice;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import de.adesso.budgeteer.service.budget.BudgetService;

import java.util.List;

public class BudgetUnitModel extends LoadableDetachableModel<List<Double>> {

    @SpringBean
    private BudgetService service;

    private long projectId;

    public BudgetUnitModel(long projectId) {
        Injector.get().inject(this);
        this.projectId = projectId;
    }

    @Override
    protected List<Double> load() {
        return service.loadBudgetUnits(projectId);
    }


}
