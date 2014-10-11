package org.wickedsource.budgeteer.web.usecase.people.details.component.budgetdistributionchart;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.statistics.BudgetValue;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;

import java.util.List;

public class BudgetDistributionChartModel extends LoadableDetachableModel<List<BudgetValue>> {

    @SpringBean
    private StatisticsService service;

    private long personId;

    public BudgetDistributionChartModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected List<BudgetValue> load() {
        return service.getBudgetDistribution(personId);
    }
}
