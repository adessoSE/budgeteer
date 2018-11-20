package org.wickedsource.budgeteer.web.pages.budgets.components.targetactualchart;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;

import java.util.Date;

public class BudgetForecastModel extends LoadableDetachableModel<TargetAndActual> implements IObjectClassAwareModel<TargetAndActual> {
    @SpringBean
    private StatisticsService service;

    private long budgetId;

    public BudgetForecastModel(long budgetId) {
        Injector.get().inject(this);
        this.budgetId = budgetId;
    }

    @Override
    protected TargetAndActual load() {
        if (budgetId != 0) {
            return service.getMonthlyForcastForBudget(budgetId, new Date());
        } else {
            throw new IllegalStateException("budgetId is not specified. Specify it in the constructor!");
        }
    }

    @Override
    public Class<TargetAndActual> getObjectClass() {
        return TargetAndActual.class;
    }
}
