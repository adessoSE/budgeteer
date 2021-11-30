package de.adesso.budgeteer.rest.budget;

import de.adesso.budgeteer.core.budget.port.in.*;
import de.adesso.budgeteer.rest.budget.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/budgets")
public class BudgetController {

    private final GetBudgetsInProjectUseCase getBudgetsInProjectUseCase;
    private final GetBudgetByIdUseCase getBudgetByIdUseCase;
    private final CreateBudgetUseCase createBudgetUseCase;
    private final UpdateBudgetUseCase updateBudgetUseCase;
    private final DeleteBudgetByIdUseCase deleteBudgetByIdUseCase;
    private final GetBudgetNoteUseCase getBudgetNoteUseCase;
    private final UpdateBudgetNoteUseCase updateBudgetNoteUseCase;
    private final BudgetModelMapper budgetModelMapper;

    @GetMapping
    public List<BudgetModel> getBudgetsInProject(@RequestParam("projectId") long projectId) {
        return budgetModelMapper.toModel(getBudgetsInProjectUseCase.getBudgetsInProject(projectId));
    }

    @GetMapping("/{budgetId}")
    public BudgetModel getBudget(@PathVariable("budgetId") long budgetId) {
        return getBudgetByIdUseCase.getBudgetById(budgetId)
                .map(budgetModelMapper::toModel)
                .orElseThrow(IllegalArgumentException::new);
    }

    @PutMapping("/{budgetId}")
    public BudgetModel updateBudget(@PathVariable("budgetId") long budgetId, @Valid @RequestBody UpdateBudgetModel updateBudgetModel) {
        return budgetModelMapper.toModel(updateBudgetUseCase.updateBudget(new UpdateBudgetUseCase.UpdateBudgetCommand(
                budgetId,
                updateBudgetModel.getContractId(),
                updateBudgetModel.getName(),
                updateBudgetModel.getDescription(),
                updateBudgetModel.getImportKey(),
                updateBudgetModel.getTotal(),
                updateBudgetModel.getLimit(),
                updateBudgetModel.getTags()
        )));
    }

    @PostMapping
    public BudgetModel createBudget(@Valid @RequestBody CreateBudgetModel createBudgetModel) {
        return budgetModelMapper.toModel(createBudgetUseCase.createBudget(new CreateBudgetUseCase.CreateBudgetCommand(
                createBudgetModel.getProjectId(),
                createBudgetModel.getContractId(),
                createBudgetModel.getName(),
                createBudgetModel.getDescription(),
                createBudgetModel.getImportKey(),
                createBudgetModel.getTotal(),
                createBudgetModel.getLimit(),
                createBudgetModel.getTags()
        )));
    }

    @DeleteMapping("/{budgetId}")
    public void deleteBudget(@PathVariable("budgetId") long budgetId) {
        deleteBudgetByIdUseCase.deleteBudgetById(budgetId);
    }

    @GetMapping("/{budgetId}/note")
    public BudgetNoteModel getBudgetNote(@PathVariable("budgetId") long budgetId) {
        return new BudgetNoteModel(getBudgetNoteUseCase.getBudgetNote(budgetId));
    }

    @PostMapping("/{budgetId}/note")
    public void updateBudgetNote(@PathVariable("budgetId") long budgetId, @RequestBody UpdateBudgetNoteModel updateBudgetNoteModel) {
        updateBudgetNoteUseCase.updateBudgetNote(budgetId, updateBudgetNoteModel.getNote());
    }
}
