package de.adesso.budgeteer.rest.contract;

import de.adesso.budgeteer.core.common.Attachment;
import de.adesso.budgeteer.core.contract.port.in.*;
import de.adesso.budgeteer.rest.contract.model.ContractModel;
import de.adesso.budgeteer.rest.contract.model.CreateContractModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<ContractModel> getContract(@PathVariable long contractId) {
        return getContractByIdUseCase.getContractById(contractId).map(contractModelMapper::toModel);
    }

    @GetMapping
    public List<ContractModel> getContractsInProject(@RequestParam long projectId) {
        return contractModelMapper.toModel(getContractsInProjectUseCase.getContractsInProject(projectId));
    }

    @DeleteMapping("/{contractId}")
    public void deleteContract(@PathVariable long contractId) {
        deleteContractUseCase.deleteContract(contractId);
    }

    @PostMapping
    public ContractModel createContract(@Valid @RequestBody CreateContractModel createContractModel) {
        var attachment = Optional.ofNullable(createContractModel.getAttachment());

        return contractModelMapper.toModel(createContractUseCase.createContract(new CreateContractUseCase.CreateContractCommand(
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
                attachment.map(Attachment::getFile).orElse(null)
        )));
    }

    @PutMapping("/{contractId}")
    public ContractModel updateContract(@Valid @RequestBody CreateContractModel createContractModel, @PathVariable long contractId) {
        var attachment = Optional.ofNullable(createContractModel.getAttachment());

        return contractModelMapper.toModel(updateContractUseCase.updateContract(new UpdateContractUseCase.UpdateContractCommand(
                contractId,
                createContractModel.getName(),
                createContractModel.getInternalNumber(),
                createContractModel.getStartDate(),
                createContractModel.getType(),
                createContractModel.getBudget(),
                createContractModel.getTaxRate(),
                createContractModel.getAttributes(),
                attachment.map(Attachment::getLink).orElse(null),
                attachment.map(Attachment::getFileName).orElse(null),
                attachment.map(Attachment::getFile).orElse(null)
        )));
    }
}
