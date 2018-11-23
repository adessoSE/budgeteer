package org.wickedsource.budgeteer.service.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.contract.ContractSortingEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractSortingRepository;
import org.wickedsource.budgeteer.persistence.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ContractSortingService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ContractSortingRepository contractSortingRepository;

    @Autowired
    ContractService contractService;

    @Autowired
    ContractRepository contractRepository;

    public List<ContractBaseData> getSortedContracts(long projectId, long userId){
        List<ContractBaseData> contractBaseData = contractService.getContractsByProject(projectId);
        for(ContractBaseData e : contractBaseData){
            Integer sortingIndex = contractSortingRepository.getSortingIndex(e.getContractId(), userId);
            if (sortingIndex == null) {
                // Create a new ContractSortingEntity if the contract has none for this user
                ContractSortingEntity sortingEntity = new ContractSortingEntity();
                sortingEntity.setSortingIndex(0);
                sortingEntity.setContract(contractRepository.findOne(e.getContractId()));
                sortingEntity.setUser(userRepository.findOne(userId));
                contractSortingRepository.save(sortingEntity);
                sortingIndex = 0;
            }
            e.setSortingIndex(sortingIndex);
        }
        return contractBaseData;
    }

    public void saveSortingIndex(ContractBaseData data, long userId){
        ContractSortingEntity contractSortingEntity = contractSortingRepository.findByContractIdAndUserId(data.getContractId(), userId);
        contractSortingEntity.setSortingIndex(data.getSortingIndex());
        contractSortingRepository.save(contractSortingEntity);
    }

    public void deleteSortingSortingEntry(ContractBaseData data, long userId){
        ContractSortingEntity contractSortingEntity = contractSortingRepository.findByContractIdAndUserId(data.getContractId(), userId);
        contractSortingRepository.delete(contractSortingEntity);
    }
}
