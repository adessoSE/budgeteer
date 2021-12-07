package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.port.in.DeleteContractUseCase;
import de.adesso.budgeteer.core.contract.port.out.DeleteContractPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteContractService implements DeleteContractUseCase {

    private final DeleteContractPort deleteContractPort;

    @Override
    public void deleteContract(long contractId) {
        deleteContractPort.deleteContract(contractId);
    }
}
