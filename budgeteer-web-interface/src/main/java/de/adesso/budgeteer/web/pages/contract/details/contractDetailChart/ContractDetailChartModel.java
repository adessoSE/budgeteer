package de.adesso.budgeteer.web.pages.contract.details.contractDetailChart;

import de.adesso.budgeteer.service.statistics.StatisticsService;
import lombok.Getter;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ContractDetailChartModel extends LoadableDetachableModel<ContractDetailBudgetChart> {

    @SpringBean
    private StatisticsService service;

    private long contractId;

    @Getter
    private int numberOfMonths;

    public ContractDetailChartModel(long contractId, int numberOfMonths) {
        Injector.get().inject(this);
        this.contractId = contractId;
        this.numberOfMonths = numberOfMonths;
    }

    @Override
    protected ContractDetailBudgetChart load() {
        return service.getMonthlyBudgetBurnedForContract(contractId, numberOfMonths);
    }

}
