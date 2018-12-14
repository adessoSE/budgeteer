package de.adesso.budgeteer.web.pages.contract.details.differenceTable;

import de.adesso.budgeteer.persistence.contract.ContractStatisticBean;
import de.adesso.budgeteer.service.statistics.StatisticsService;
import lombok.Getter;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class DifferenceTableModel extends LoadableDetachableModel<List<ContractStatisticBean>> {
    @SpringBean
    private StatisticsService service;

    private long contractId;

    @Getter
    private Date startDate;

    public DifferenceTableModel(long contractId, Date startDate) {
        Injector.get().inject(this);
        this.contractId = contractId;
        this.startDate = startDate;
    }

    @Override
    protected List<ContractStatisticBean> load() {
        return service.getMonthlyStatisticsForContract(contractId, startDate);
    }
}
