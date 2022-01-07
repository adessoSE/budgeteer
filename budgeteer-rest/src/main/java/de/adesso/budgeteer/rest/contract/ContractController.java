package de.adesso.budgeteer.rest.contract;

import de.adesso.budgeteer.core.common.Attachment;
import de.adesso.budgeteer.core.contract.port.in.*;
import de.adesso.budgeteer.rest.contract.model.ContractIdModel;
import de.adesso.budgeteer.rest.contract.model.ContractModel;
import de.adesso.budgeteer.rest.contract.model.CreateContractModel;
import de.adesso.budgeteer.rest.project.model.ProjectIdModel;
import de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToContract;
import de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToProject;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@HasAccessToContract
@HasAccessToProject
@RequestMapping("/contracts")
public class ContractController {
  private final GetContractByIdUseCase getContractByIdUseCase;
  private final GetContractsInProjectUseCase getContractsInProjectUseCase;
  private final DeleteContractUseCase deleteContractUseCase;
  private final CreateContractUseCase createContractUseCase;
  private final UpdateContractUseCase updateContractUseCase;
  private final ContractModelMapper contractModelMapper;

  @GetMapping("/{contractId}")
  public Optional<ContractModel> getContract(
      @PathVariable("contractId") ContractIdModel contractIdModel) {
    return getContractByIdUseCase
        .getContractById(contractIdModel.getValue())
        .map(contractModelMapper::toModel);
  }

  @GetMapping
  public List<ContractModel> getContractsInProject(
      @RequestParam("projectId") ProjectIdModel projectIdModel) {
    return contractModelMapper.toModel(
        getContractsInProjectUseCase.getContractsInProject(projectIdModel.getValue()));
  }

  @DeleteMapping("/{contractId}")
  public void deleteContract(@PathVariable("contractId") ContractIdModel contractIdModel) {
    deleteContractUseCase.deleteContract(contractIdModel.getValue());
  }

  @PostMapping
  public ContractModel createContract(
      @RequestParam("projectId") ProjectIdModel projectIdModel,
      @Valid @RequestBody CreateContractModel createContractModel) {
    var attachment = Optional.ofNullable(createContractModel.getAttachment());

    return contractModelMapper.toModel(
        createContractUseCase.createContract(
            new CreateContractUseCase.CreateContractCommand(
                projectIdModel.getValue(),
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
