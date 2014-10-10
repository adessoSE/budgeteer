package org.wickedsource.budgeteer.web.usecase.dashboard.component.burnedbudgetchart;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;

import java.util.List;

public class BurnedBudgetChartModel extends LoadableDetachableModel<List<Double>> {

    @SpringBean
    private StatisticsService service;

    private long userId;

    private int numberOfWeeks;

    public BurnedBudgetChartModel(long userId, int numberOfWeeks) {
        Injector.get().inject(this);
        this.userId = userId;
        this.numberOfWeeks = numberOfWeeks;
    }

    @Override
    protected List<Double> load() {
        return service.getBudgetBurnedInPreviousWeeks(userId, numberOfWeeks);
    }

    public long getNumberOfWeeks() {
        return this.numberOfWeeks;
    }
}
