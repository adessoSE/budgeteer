package org.wickedsource.budgeteer.web.pages.people.details.chart;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.statistics.Share;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;

import java.util.List;

public class BudgetDistributionChartModel extends LoadableDetachableModel<List<Share>> {

    @SpringBean
    private StatisticsService service;

    private long personId;

    public BudgetDistributionChartModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected List<Share> load() {
        return service.getBudgetDistribution(personId);
    }
}
