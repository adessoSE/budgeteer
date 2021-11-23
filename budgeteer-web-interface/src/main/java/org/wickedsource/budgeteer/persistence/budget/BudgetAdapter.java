package org.wickedsource.budgeteer.persistence.budget;

import de.adesso.budgeteer.core.budget.domain.Budget;
import de.adesso.budgeteer.core.budget.port.out.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BudgetAdapter implements
        GetBudgetsInProjectPort,
        GetBudgetByIdPort,
        CreateBudgetEntityPort,
        DeleteBudgetByIdPort,
        UpdateBudgetEntityPort,
        GetTagsInProjectPort,
        GetBudgetNotePort,
        UpdateBudgetNotePort,
        IsUniqueImportKeyInProjectPort,
        IsUniqueNameInProjectPort {

    private final BudgetRepository budgetRepository;
    private final ProjectRepository projectRepository;
    private final ContractRepository contractRepository;
    private final BudgetMapper budgetMapper;

    @Transactional
    @Override
    public List<Budget> getBudgetsInProject(long projectId) {
        return budgetMapper.mapToDomain(budgetRepository.findByProjectIdOrderByNameAsc(projectId));
    }

    @Transactional
    @Override
    public Optional<Budget> getBudgetById(long budgetId) {
        return budgetRepository.findById(budgetId).map(budgetMapper::mapToDomain);
    }

    @Transactional
    @Override
    public Budget createBudgetEntity(CreateBudgetCommandEntity command) {
        var budgetEntity = new BudgetEntity();
        var projectEntity = projectRepository.findById(command.getProjectId()).orElseThrow();
        var contractEntity = command.getContractId().flatMap(contractRepository::findById).orElse(null);
        budgetEntity.setProject(projectEntity);
        budgetEntity.setContract(contractEntity);
        budgetEntity.setName(command.getName());
        budgetEntity.setDescription(command.getDescription());
        budgetEntity.setImportKey(command.getImportKey());
        budgetEntity.setTotal(command.getTotal());
        budgetEntity.setLimit(command.getLimit());
        budgetEntity.setTags(mapToTagEntities(command.getTags(), budgetEntity));
        return budgetMapper.mapToDomain(budgetRepository.save(budgetEntity));
    }

    @Transactional
    @Override
    public void deleteBudgetById(long budgetId) {
        budgetRepository.deleteById(budgetId);
    }

    @Transactional
    @Override
    public Budget updateBudgetEntity(UpdateBudgetEntityCommand command) {
        var budgetEntity = budgetRepository.findById(command.getBudgetId()).orElseThrow();
        var tags = mapToTagEntities(command.getTags(), budgetEntity);
        if (command.getContractId() != null && budgetEntity.getContract().getId() != command.getContractId()) {
            budgetEntity.setContract(contractRepository.findById(command.getContractId()).orElseThrow());
        }
        budgetEntity.setName(command.getName());
        budgetEntity.setDescription(command.getDescription());
        budgetEntity.setImportKey(command.getImportKey());
        budgetEntity.setTotal(command.getTotal());
        budgetEntity.setLimit(command.getLimit());
        budgetEntity.getTags().clear();
        budgetEntity.getTags().addAll(tags);
        return budgetMapper.mapToDomain(budgetRepository.save(budgetEntity));
    }

    @Override
    public List<String> getTagsInProject(long projectId) {
        return budgetRepository.getAllTagsInProject(projectId);
    }

    @Override
    @Transactional
    public String getBudgetNote(long budgetId) {
        return budgetRepository.getNoteForBudget(budgetId);
    }

    @Override
    @Transactional
    public void updateBudgetNote(long budgetId, String note) {
        budgetRepository.updateNoteForBudget(budgetId, note);
    }

    @Override
    public boolean isUniqueImportKeyInProject(long projectId, String importKey) {
        return !budgetRepository.existsByImportKeyAndProjectId(importKey, projectId);
    }

    @Override
    public boolean isUniqueImportKeyInProjectByBudgetId(long budgetId, String importKey) {
        return !budgetRepository.existsWithImportKeyInProjectByBudgetId(budgetId, importKey);
    }

    @Override
    public boolean isUniqueNameInProject(long projectId, String name) {
        return !budgetRepository.existsByNameAndProjectId(name, projectId);
    }

    @Override
    public boolean isUniqueNameInProjectByBudgetId(long budgetId, String name) {
        return !budgetRepository.existsWithNameInProjectByBudgetId(budgetId, name);
    }

    private List<BudgetTagEntity> mapToTagEntities(List<String> tags, BudgetEntity budgetEntity) {
        return tags.stream().map(tag -> new BudgetTagEntity(budgetEntity, tag)).collect(Collectors.toList());
    }
}
