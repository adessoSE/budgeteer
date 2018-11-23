package org.wickedsource.budgeteer.web.pages.budgets.components.targetactualchart;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;

public class BudgetForecastModel extends LoadableDetachableModel<TargetAndActual> implements IObjectClassAwareModel<TargetAndActual> {
    @SpringBean
    private StatisticsService service;

    private boolean isSingleBudgetForecast;
    private long id;

    /**
     * Creates a forecast model for a single budget (defined by a budgetId) or all budgets of a project(defined by a projectId)
     *
     * @param id                     project or budget ID
     * @param isSingleBudgetForecast defines if the model is for a single budget or multiple budgets
     */
    public BudgetForecastModel(long id, boolean isSingleBudgetForecast) {
        Injector.get().inject(this);
        this.id = id;
        this.isSingleBudgetForecast = isSingleBudgetForecast;
    }

    @Override
    protected TargetAndActual load() {
        if (isSingleBudgetForecast) {
            return service.getMonthlyForecastForBudget(id, DateUtil.getEndOfThisMonth());
        } else {
            return service.getMonthlyForecastForBudgets(id, DateUtil.getEndOfThisMonth());
        }
    }

    @Override
    public Class<TargetAndActual> getObjectClass() {
        return TargetAndActual.class;
    }
}
