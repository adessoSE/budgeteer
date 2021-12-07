package de.adesso.budgeteer.core.contract.service;

import de.adesso.budgeteer.core.contract.port.in.UserHasAccessToContractUseCase;
import de.adesso.budgeteer.core.contract.port.out.UserHasAccessToContractPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHasAccessToContractService implements UserHasAccessToContractUseCase {
    private final UserHasAccessToContractPort userHasAccessToContractPort;

    @Override
    public boolean userHasAccessToContract(String username, long contractId) {
        return userHasAccessToContractPort.userHasAccessToContract(username, contractId);
    }
}
