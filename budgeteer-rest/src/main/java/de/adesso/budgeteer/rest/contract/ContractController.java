package de.adesso.budgeteer.rest.contract;

import de.adesso.budgeteer.core.common.Attachment;
import de.adesso.budgeteer.core.contract.port.in.*;
import de.adesso.budgeteer.rest.contract.model.ContractIdModel;
import de.adesso.budgeteer.rest.contract.model.ContractModel;
import de.adesso.budgeteer.rest.contract.model.CreateContractModel;
import de.adesso.budgeteer.rest.project.model.ProjectIdModel;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contracts")
public class ContractController {
  private final GetContractByIdUseCase getContractByIdUseCase;
  private final GetContractsInProjectUseCase getContractsInProjectUseCase;
  private final DeleteContractUseCase deleteContractUseCase;
  private final CreateContractUseCase createContractUseCase;
  private final UpdateContractUseCase updateContractUseCase;
  private final ContractModelMapper contractModelMapper;

  @GetMapping("/{contractId}")
  @PreAuthorize("userHasAccesToContract(#contractIdModel.value)")
  public Optional<ContractModel> getContract(
      @PathVariable("contractId") ContractIdModel contractIdModel) {
    return getContractByIdUseCase
        .getContractById(contractIdModel.getValue())
        .map(contractModelMapper::toModel);
  }

  @GetMapping
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
  public List<ContractModel> getContractsInProject(
      @RequestParam("projectId") ProjectIdModel projectIdModel) {
    return contractModelMapper.toModel(
        getContractsInProjectUseCase.getContractsInProject(projectIdModel.getValue()));
  }

  @DeleteMapping("/{contractId}")
  @PreAuthorize("userHasAccessToContract(#contractIdModel.value)")
  public void deleteContract(@PathVariable("contractId") ContractIdModel contractIdModel) {
    deleteContractUseCase.deleteContract(contractIdModel.getValue());
  }

  @PostMapping
  @PreAuthorize("userHasAccessToProject(#createContractModel.projectId)")
  public ContractModel createContract(@Valid @RequestBody CreateContractModel createContractModel) {
    var attachment = Optional.ofNullable(createContractModel.getAttachment());

    return contractModelMapper.toModel(
        createContractUseCase.createContract(
            new CreateContractUseCase.CreateContractCommand(
                createContractModel.getProjectId(),
                createContractModel.getName(),
                createContractModel.getInternalNumber(),
                createContractModel.getStartDate(),
                createContractModel.getType(),
                createContractModel.getBudget(),
                createContractModel.getTaxRate(),
                createContractModel.getAttributes(),
                attachment.map(Attachment::getLink).orElse(null),
                attachment.map(Attachment::getFileName).orElse(null),
                attachment.map(Attachment::getFile).orElse(null))));
  }

  @PutMapping("/{contractId}")
  @PreAuthorize("userHasAccesToContract(#contractIdModel.value)")
  public ContractModel updateContract(
      @PathVariable("contractId") ContractIdModel contractIdModel,
      @Valid @RequestBody CreateContractModel createContractModel) {
    var attachment = Optional.ofNullable(createContractModel.getAttachment());

    return contractModelMapper.toModel(
        updateContractUseCase.updateContract(
            new UpdateContractUseCase.UpdateContractCommand(
                contractIdModel.getValue(),
                createContractModel.getName(),
                createContractModel.getInternalNumber(),
                createContractModel.getStartDate(),
                createContractModel.getType(),
                createContractModel.getBudget(),
                createContractModel.getTaxRate(),
                createContractModel.getAttributes(),
                attachment.map(Attachment::getLink).orElse(null),
                attachment.map(Attachment::getFileName).orElse(null),
                attachment.map(Attachment::getFile).orElse(null))));
  }
}
