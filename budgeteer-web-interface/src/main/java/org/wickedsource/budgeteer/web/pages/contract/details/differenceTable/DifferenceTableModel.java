package org.wickedsource.budgeteer.web.pages.contract.details.differenceTable;

import lombok.Getter;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.persistence.contract.ContractStatisticBean;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;

import java.util.List;

public class DifferenceTableModel extends LoadableDetachableModel<List<ContractStatisticBean>> {
    @SpringBean
    private StatisticsService service;

    private long contractId;

    @Getter
    private int numberOfMonths;

    public DifferenceTableModel(long contractId, int numberOfMonths) {
        Injector.get().inject(this);
        this.contractId = contractId;
        this.numberOfMonths = numberOfMonths;
    }

    @Override
    protected List<ContractStatisticBean> load() {
        return service.getMonthlyStatisticsForContract(contractId, numberOfMonths);
    }
}
