package org.wickedsource.budgeteer.web.usecase.dashboard.component.dailyratechart;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;

import java.util.List;

public class AverageDailyRateChartModel extends LoadableDetachableModel<List<Double>> {

    @SpringBean
    private StatisticsService service;

    private long userId;

    private int numberOfDays;

    public AverageDailyRateChartModel(long userId, int numberOfDays) {
        this.userId = userId;
        this.numberOfDays = numberOfDays;
        Injector.get().inject(this);
    }

    @Override
    protected List<Double> load() {
        return service.getAvgDailyRateForPreviousDays(userId, numberOfDays);
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }
}
