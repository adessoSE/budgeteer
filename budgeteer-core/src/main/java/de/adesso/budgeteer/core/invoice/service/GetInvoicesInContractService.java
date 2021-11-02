package de.adesso.budgeteer.core.invoice.service;

import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.core.invoice.port.in.GetInvoicesInContractUseCase;
import de.adesso.budgeteer.core.invoice.port.out.GetInvoicesInContractPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetInvoicesInContractService implements GetInvoicesInContractUseCase {
    private final GetInvoicesInContractPort getInvoicesInContractPort;

    @Override
    public List<Invoice> getInvoicesInContract(long contractId) {
        return getInvoicesInContractPort.getInvoicesInContract(contractId);
    }
}
