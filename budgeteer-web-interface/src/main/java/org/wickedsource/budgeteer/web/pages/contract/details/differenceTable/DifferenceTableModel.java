package org.wickedsource.budgeteer.web.pages.contract.details.differenceTable;

import java.util.Date;
import java.util.List;

import lombok.Getter;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.persistence.contract.ContractStatisticBean;
import org.wickedsource.budgeteer.service.statistics.StatisticsService;

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
