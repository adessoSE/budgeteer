package org.wickedsource.budgeteer.web.pages.dashboard.burnedbudgetchart;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.money.Money;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;

import java.util.List;

public class BurnedBudgetChartModel extends LoadableDetachableModel<List<Money>> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean
    private StatisticsService service;

    private long projectId;

    private int numberOfWeeks;

    public BurnedBudgetChartModel(long projectId, int numberOfWeeks) {
        Injector.get().inject(this);
        this.projectId = projectId;
        this.numberOfWeeks = numberOfWeeks;
    }

    @Override
    protected List<Money> load() {
        return service.getWeeklyBudgetBurnedForProject(projectId, numberOfWeeks);
    }

    public int getNumberOfWeeks() {
        return this.numberOfWeeks;
    }
}
