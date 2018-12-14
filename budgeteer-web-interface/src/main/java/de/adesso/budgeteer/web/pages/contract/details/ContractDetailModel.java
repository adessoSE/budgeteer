package de.adesso.budgeteer.web.pages.contract.details;

import de.adesso.budgeteer.service.contract.ContractBaseData;
import de.adesso.budgeteer.service.contract.ContractService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ContractDetailModel extends LoadableDetachableModel<ContractBaseData>{

    @SpringBean
    private ContractService service;

    private long contractId;

    public ContractDetailModel(long contractId) {
        Injector.get().inject(this);
        this.contractId = contractId;
    }

    @Override
    protected ContractBaseData load() {
        return service.getContractById(contractId);
    }

}
