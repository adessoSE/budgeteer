package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.port.in.GetContractByIdUseCase;
import de.adesso.budgeteer.core.contract.port.out.GetContractByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetContractByIdService implements GetContractByIdUseCase {

    private final GetContractByIdPort getContractByIdPort;

    @Override
    public Optional<Contract> getContractById(long id) {
        return getContractByIdPort.getContractById(id);
    }
}
