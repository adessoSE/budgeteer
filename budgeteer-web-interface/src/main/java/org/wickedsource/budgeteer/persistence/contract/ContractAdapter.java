package org.wickedsource.budgeteer.persistence.contract;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.domain.ContractSummary;
import de.adesso.budgeteer.core.contract.port.out.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.invoice.InvoiceRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectContractField;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContractAdapter implements
        GetContractsInProjectPort,
        CreateContractEntityPort,
        GetContractByIdPort,
        UpdateContractEntityPort,
        DeleteContractPort,
        GetContractSummariesInRangePort {

    private final ContractRepository contractRepository;
    private final ProjectRepository projectRepository;
    private final InvoiceRepository invoiceRepository;
    private final BudgetRepository budgetRepository;
    private final ContractMapper contractMapper;

    @Override
    @Transactional
    public List<Contract> getContractsInProject(long projectId) {
        return contractMapper.mapToDomain(contractRepository.findByProjectId(projectId));
    }

    @Override
    @Transactional
    public void createContractEntity(CreateContractEntityCommand command) {
        var contractEntity = new ContractEntity();
        var projectEntity = projectRepository.findById(command.getProjectId()).orElseThrow();

        contractEntity.setProject(projectRepository.findById(command.getProjectId()).orElseThrow());
        contractEntity.setInternalNumber(command.getInternalNumber());
        contractEntity.setName(command.getName());
        contractEntity.setType(command.getType() == Contract.Type.TIME_AND_MATERIAL ? ContractEntity.ContractType.T_UND_M : ContractEntity.ContractType.FIXED_PRICE);
        contractEntity.setStartDate(command.getStartDate());
        contractEntity.setBudget(command.getBudget());
        contractEntity.setTaxRate(command.getTaxRate());
        contractEntity.setContractFields(addContractFieldsOrCreateProjectContractPairs(projectEntity.getId(), command.getAttributes(), projectEntity));
        contractEntity.setLink(command.getLink());
        contractEntity.setFileName(command.getFileName());
        contractEntity.setFile(command.getFile());

        contractRepository.save(contractEntity);
    }

    @Override
    @Transactional
    public Optional<Contract> getContractById(long contractId) {
        return contractRepository.findById(contractId).map(contractMapper::mapToDomain);
    }

    @Override
    @Transactional
    public void updateContractEntity(UpdateContractEntityCommand command) {
        var contractEntity = contractRepository.findById(command.getContractId()).orElseThrow();
        contractEntity.setName(command.getName());
        contractEntity.setInternalNumber(command.getInternalNumber());
        contractEntity.setStartDate(command.getStartDate());
        contractEntity.setType(command.getType() == Contract.Type.TIME_AND_MATERIAL ? ContractEntity.ContractType.T_UND_M : ContractEntity.ContractType.FIXED_PRICE);
        contractEntity.setBudget(command.getBudget());
        contractEntity.setTaxRate(command.getTaxRate());
        // By updating existing ContractFields and then only creating new fields for actually new ones
        // we greatly reduce the amount of queries we have to do.
        contractEntity.getContractFields().stream()
                .filter(field -> command.getAttributes().containsKey(field.getField().getFieldName()))
                .forEach(field -> field.setValue(command.getAttributes().remove(field.getField().getFieldName())));
        var newAttributes = command.getAttributes().entrySet().stream()
                .map(attribute -> new ContractFieldEntity(new ProjectContractField(attribute.getKey(), contractEntity.getProject()), attribute.getValue()))
                .collect(Collectors.toList());
        contractEntity.getContractFields().addAll(newAttributes);
        contractEntity.setLink(command.getLink());
        contractEntity.setFileName(command.getFileName());
        contractEntity.setFile(command.getFile());
        contractRepository.save(contractEntity);
    }

    @Override
    @Transactional
    public List<ContractSummary> getContractSummariesInRange(long projectId, LocalDate from, LocalDate until) {
        return contractMapper.mapToContractSummaries(contractRepository.findByProjectId(projectId), from, until);
    }

    @Override
    @Transactional
    public void deleteContract(long contractId) {
        budgetRepository.removeReferencesToContract(contractId);
        invoiceRepository.deleteInvoiceFieldsByContractId(contractId);
        invoiceRepository.deleteInvoicesByContractId(contractId);
        contractRepository.deleteById(contractId);
    }

    private List<ContractFieldEntity> addContractFieldsOrCreateProjectContractPairs(long projectId, Map<String, String> attributes, ProjectEntity projectEntity) {
        return attributes.entrySet().stream()
                .map(attribute -> addContractFieldOrCreateProjectContractPair(projectId, attribute, projectEntity))
                .collect(Collectors.toList());
    }

    private ContractFieldEntity addContractFieldOrCreateProjectContractPair(long projectId, Map.Entry<String, String> attribute, ProjectEntity project) {
        var projectContractField = Optional.ofNullable(projectRepository.findContractFieldByName(projectId, attribute.getKey().trim()))
                .orElse(new ProjectContractField(attribute.getKey().trim(), project));
        return new ContractFieldEntity(projectContractField, StringUtils.trimToEmpty(attribute.getValue()));
    }
}
