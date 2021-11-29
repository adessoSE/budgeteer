package de.adesso.budgeteer.rest.invoice;

import de.adesso.budgeteer.core.invoice.port.in.*;
import de.adesso.budgeteer.rest.invoice.model.CreateInvoiceModel;
import de.adesso.budgeteer.rest.invoice.model.InvoiceModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invoices")
public class InvoiceController {
    private final GetInvoiceByIdUseCase getInvoiceByIdUseCase;
    private final GetInvoicesInContractUseCase getInvoicesInContractUseCase;
    private final GetInvoicesInProjectUseCase getInvoicesInProjectUseCase;
    private final DeleteInvoiceByIdUseCase deleteInvoiceByIdUseCase;
    private final CreateInvoiceUseCase createInvoiceUseCase;
    private final UpdateInvoiceUseCase updateInvoiceUseCase;
    private final InvoiceModelMapper invoiceModelMapper;

    @GetMapping("/{invoiceId}")
    public Optional<InvoiceModel> getInvoice(@PathVariable long invoiceId) {
        return getInvoiceByIdUseCase.getInvoiceById(invoiceId).map(invoiceModelMapper::toModel);
    }

    @GetMapping("/byContract/{contractId}")
    public List<InvoiceModel> getInvoicesInContract(@PathVariable long contractId) {
        return invoiceModelMapper.toModel(getInvoicesInContractUseCase.getInvoicesInContract(contractId));
    }

    @GetMapping("/byProject/{projectId}")
    public List<InvoiceModel> getInvoicesInProject(@PathVariable long projectId) {
        return invoiceModelMapper.toModel(getInvoicesInProjectUseCase.getInvoicesInProject(projectId));
    }

    @DeleteMapping("/{invoiceId}")
    public void deleteInvoice(@PathVariable long invoiceId) {
        deleteInvoiceByIdUseCase.deleteInvoiceById(invoiceId);
    }

    @PostMapping
    public InvoiceModel createInvoice(@Valid @RequestBody CreateInvoiceModel createInvoiceModel) {
        return invoiceModelMapper.toModel(createInvoiceUseCase.createInvoice(new CreateInvoiceUseCase.CreateInvoiceCommand(
                createInvoiceModel.getContractId(),
                createInvoiceModel.getName(),
                createInvoiceModel.getAmountOwed(),
                createInvoiceModel.getTaxRate(),
                createInvoiceModel.getInternalNumber(),
                createInvoiceModel.getYearMonth(),
                createInvoiceModel.getPaidDate(),
                createInvoiceModel.getDueDate(),
                createInvoiceModel.getAttributes(),
                createInvoiceModel.getLink(),
                createInvoiceModel.getFile()
        )));
    }

    @PutMapping("/{invoiceId}")
    public InvoiceModel updateInvoice(@Valid @RequestBody CreateInvoiceModel createInvoiceModel, @PathVariable long invoiceId) {
        return invoiceModelMapper.toModel(updateInvoiceUseCase.updateInvoice(new UpdateInvoiceUseCase.UpdateInvoiceCommand(
                invoiceId,
                createInvoiceModel.getContractId(),
                createInvoiceModel.getName(),
                createInvoiceModel.getAmountOwed(),
                createInvoiceModel.getTaxRate(),
                createInvoiceModel.getInternalNumber(),
                createInvoiceModel.getYearMonth(),
                createInvoiceModel.getPaidDate(),
                createInvoiceModel.getDueDate(),
                createInvoiceModel.getAttributes(),
                createInvoiceModel.getLink(),
                createInvoiceModel.getFile()
        )));
    }
}
