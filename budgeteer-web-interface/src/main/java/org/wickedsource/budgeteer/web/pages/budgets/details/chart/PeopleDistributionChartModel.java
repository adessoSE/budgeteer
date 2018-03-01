package org.wickedsource.budgeteer.web.pages.budgets.details.chart;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.statistics.Share;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;

import java.util.List;

public class PeopleDistributionChartModel extends LoadableDetachableModel<List<Share>> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean
    private StatisticsService service;

    private long budgetId;

    public PeopleDistributionChartModel(long budgetId) {
        Injector.get().inject(this);
        this.budgetId = budgetId;
    }

    @Override
    protected List<Share> load() {
        return service.getPeopleDistribution(budgetId);
    }
}
