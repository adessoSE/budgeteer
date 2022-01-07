package de.adesso.budgeteer.rest.invoice;

import de.adesso.budgeteer.core.invoice.port.in.*;
import de.adesso.budgeteer.rest.contract.model.ContractIdModel;
import de.adesso.budgeteer.rest.invoice.model.CreateInvoiceModel;
import de.adesso.budgeteer.rest.invoice.model.InvoiceIdModel;
import de.adesso.budgeteer.rest.invoice.model.InvoiceModel;
import de.adesso.budgeteer.rest.invoice.model.UpdateInvoiceModel;
import de.adesso.budgeteer.rest.project.model.ProjectIdModel;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
  @PreAuthorize("userHasAccessToInvoice(#invoiceIdModel.value)")
  public Optional<InvoiceModel> getInvoice(
      @PathVariable("invoiceId") InvoiceIdModel invoiceIdModel) {
    return getInvoiceByIdUseCase
        .getInvoiceById(invoiceIdModel.getValue())
        .map(invoiceModelMapper::toModel);
  }

  @GetMapping("/byContract/{contractId}")
  @PreAuthorize("userHasAccessToContract(#contractIdModel.value)")
  public List<InvoiceModel> getInvoicesInContract(
      @PathVariable("contractId") ContractIdModel contractIdModel) {
    return invoiceModelMapper.toModel(
        getInvoicesInContractUseCase.getInvoicesInContract(contractIdModel.getValue()));
  }

  @GetMapping("/byProject/{projectId}")
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
  public List<InvoiceModel> getInvoicesInProject(
      @PathVariable("projectId") ProjectIdModel projectIdModel) {
    return invoiceModelMapper.toModel(
        getInvoicesInProjectUseCase.getInvoicesInProject(projectIdModel.getValue()));
  }

  @DeleteMapping("/{invoiceId}")
  @PreAuthorize("userHasAccessToInvoice(#invoiceIdModel.value)")
  public void deleteInvoice(@PathVariable("invoiceId") InvoiceIdModel invoiceIdModel) {
    deleteInvoiceByIdUseCase.deleteInvoiceById(invoiceIdModel.getValue());
  }

  @PostMapping
  @PreAuthorize("userHasAccessToContract(#createInvoiceModel.contractId)")
  public InvoiceModel createInvoice(@Valid @RequestBody CreateInvoiceModel createInvoiceModel) {
    return invoiceModelMapper.toModel(
        createInvoiceUseCase.createInvoice(
            new CreateInvoiceUseCase.CreateInvoiceCommand(
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
                createInvoiceModel.getFile())));
  }

  @PutMapping("/{invoiceId}")
  @PreAuthorize("userHasAccessToInvoice(#invoiceIdModel.value)")
  public InvoiceModel updateInvoice(
      @PathVariable("invoiceId") InvoiceIdModel invoiceIdModel,
      @Valid @RequestBody UpdateInvoiceModel updateInvoiceModel) {
    return invoiceModelMapper.toModel(
        updateInvoiceUseCase.updateInvoice(
            new UpdateInvoiceUseCase.UpdateInvoiceCommand(
                invoiceIdModel.getValue(),
                updateInvoiceModel.getContractId(),
                updateInvoiceModel.getName(),
                updateInvoiceModel.getAmountOwed(),
                updateInvoiceModel.getTaxRate(),
                updateInvoiceModel.getInternalNumber(),
                updateInvoiceModel.getYearMonth(),
                updateInvoiceModel.getPaidDate(),
                updateInvoiceModel.getDueDate(),
                updateInvoiceModel.getAttributes(),
                updateInvoiceModel.getLink(),
                updateInvoiceModel.getFile())));
  }
}
