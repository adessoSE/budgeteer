package de.adesso.budgeteer.rest.budget;

import de.adesso.budgeteer.core.budget.port.in.*;
import de.adesso.budgeteer.rest.budget.model.*;
import de.adesso.budgeteer.rest.project.model.ProjectIdModel;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
  public List<BudgetModel> getBudgetsInProject(
      @RequestParam("projectId") ProjectIdModel projectIdModel) {
    return budgetModelMapper.toModel(
        getBudgetsInProjectUseCase.getBudgetsInProject(projectIdModel.getValue()));
  }

  @GetMapping("/{budgetId}")
  @PreAuthorize("userHasAccessToBudget(#budgetIdModel.value)")
  public BudgetModel getBudget(@PathVariable("budgetId") BudgetIdModel budgetIdModel) {
    return getBudgetByIdUseCase
        .getBudgetById(budgetIdModel.getValue())
        .map(budgetModelMapper::toModel)
        .orElseThrow(IllegalArgumentException::new);
  }

  @PutMapping("/{budgetId}")
  @PreAuthorize("userHasAccessToBudget(#budgetIdModel.value)")
  public BudgetModel updateBudget(
      @PathVariable("budgetId") BudgetIdModel budgetIdModel,
      @Valid @RequestBody UpdateBudgetModel updateBudgetModel) {
    return budgetModelMapper.toModel(
        updateBudgetUseCase.updateBudget(
            new UpdateBudgetUseCase.UpdateBudgetCommand(
                budgetIdModel.getValue(),
                updateBudgetModel.getContractId(),
                updateBudgetModel.getName(),
                updateBudgetModel.getDescription(),
                updateBudgetModel.getImportKey(),
                updateBudgetModel.getTotal(),
                updateBudgetModel.getLimit(),
                updateBudgetModel.getTags())));
  }

  @PostMapping
  @PreAuthorize("userHasAccessToProject(#createBudgetModel.projectId)")
  public BudgetModel createBudget(@Valid @RequestBody CreateBudgetModel createBudgetModel) {
    return budgetModelMapper.toModel(
        createBudgetUseCase.createBudget(
            new CreateBudgetUseCase.CreateBudgetCommand(
                createBudgetModel.getProjectId(),
                createBudgetModel.getContractId(),
                createBudgetModel.getName(),
                createBudgetModel.getDescription(),
                createBudgetModel.getImportKey(),
                createBudgetModel.getTotal(),
                createBudgetModel.getLimit(),
                createBudgetModel.getTags())));
  }

  @DeleteMapping("/{budgetId}")
  @PreAuthorize("userHasAccessToBudget(#budgetIdModel.value)")
  public void deleteBudget(@PathVariable("budgetId") BudgetIdModel budgetIdModel) {
    deleteBudgetByIdUseCase.deleteBudgetById(budgetIdModel.getValue());
  }

  @GetMapping("/{budgetId}/note")
  @PreAuthorize("userHasAccessToBudget(#budgetIdModel.value)")
  public BudgetNoteModel getBudgetNote(@PathVariable("budgetId") BudgetIdModel budgetIdModel) {
    return new BudgetNoteModel(getBudgetNoteUseCase.getBudgetNote(budgetIdModel.getValue()));
  }

  @PostMapping("/{budgetId}/note")
  @PreAuthorize("userHasAccessToBudget(#budgetIdModel.value)")
  public void updateBudgetNote(
      @PathVariable("budgetId") BudgetIdModel budgetIdModel,
      @RequestBody UpdateBudgetNoteModel updateBudgetNoteModel) {
    updateBudgetNoteUseCase.updateBudgetNote(
        budgetIdModel.getValue(), updateBudgetNoteModel.getNote());
  }
}
