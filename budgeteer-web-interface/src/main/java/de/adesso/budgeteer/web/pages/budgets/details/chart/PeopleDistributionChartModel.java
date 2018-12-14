package de.adesso.budgeteer.web.pages.budgets.details.chart;

import de.adesso.budgeteer.service.statistics.Share;
import de.adesso.budgeteer.service.statistics.StatisticsService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
