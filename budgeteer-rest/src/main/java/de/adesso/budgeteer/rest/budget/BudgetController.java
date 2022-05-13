package de.adesso.budgeteer.rest.budget;

import de.adesso.budgeteer.core.budget.port.in.*;
import de.adesso.budgeteer.rest.budget.model.*;
import de.adesso.budgeteer.rest.contract.model.ContractIdModel;
import de.adesso.budgeteer.rest.project.model.ProjectIdModel;
import de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToBudget;
import de.adesso.budgeteer.rest.security.authorization.aspects.annotations.HasAccessToProject;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/budgets")
@HasAccessToBudget
@HasAccessToProject
public class BudgetController {

  private final GetBudgetsInProjectUseCase getBudgetsInProjectUseCase;
  private final GetBudgetsInContractUseCase getBudgetsInContractUseCase;
  private final GetBudgetByIdUseCase getBudgetByIdUseCase;
  private final CreateBudgetUseCase createBudgetUseCase;
  private final UpdateBudgetUseCase updateBudgetUseCase;
  private final DeleteBudgetByIdUseCase deleteBudgetByIdUseCase;
  private final GetBudgetNoteUseCase getBudgetNoteUseCase;
  private final UpdateBudgetNoteUseCase updateBudgetNoteUseCase;
  private final BudgetModelMapper budgetModelMapper;

  @GetMapping
  public List<BudgetModel> getBudgetsInProject(
      @RequestParam("projectId") ProjectIdModel projectIdModel) {
    return budgetModelMapper.toModel(
        getBudgetsInProjectUseCase.getBudgetsInProject(projectIdModel.getValue()));
  }

  @GetMapping("/byContract")
  public List<BudgetModel> getBudgetsInContract(
      @RequestParam("contractId") ContractIdModel contractIdModel) {
    return budgetModelMapper.toModel(
        getBudgetsInContractUseCase.getBudgetsInContract(contractIdModel.getValue()));
  }

  @GetMapping("/{budgetId}")
  public BudgetModel getBudget(@PathVariable("budgetId") BudgetIdModel budgetIdModel) {
    return getBudgetByIdUseCase
        .getBudgetById(budgetIdModel.getValue())
        .map(budgetModelMapper::toModel)
        .orElseThrow(IllegalArgumentException::new);
  }

  @PutMapping("/{budgetId}")
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
  public BudgetModel createBudget(
      @RequestParam("projectId") ProjectIdModel projectIdModel,
      @Valid @RequestBody CreateBudgetModel createBudgetModel) {
    return budgetModelMapper.toModel(
        createBudgetUseCase.createBudget(
            new CreateBudgetUseCase.CreateBudgetCommand(
                projectIdModel.getValue(),
                createBudgetModel.getContractId(),
                createBudgetModel.getName(),
                createBudgetModel.getDescription(),
                createBudgetModel.getImportKey(),
                createBudgetModel.getTotal(),
                createBudgetModel.getLimit(),
                createBudgetModel.getTags())));
  }

  @DeleteMapping("/{budgetId}")
  public void deleteBudget(@PathVariable("budgetId") BudgetIdModel budgetIdModel) {
    deleteBudgetByIdUseCase.deleteBudgetById(budgetIdModel.getValue());
  }

  @GetMapping("/{budgetId}/note")
  public BudgetNoteModel getBudgetNote(@PathVariable("budgetId") BudgetIdModel budgetIdModel) {
    return new BudgetNoteModel(getBudgetNoteUseCase.getBudgetNote(budgetIdModel.getValue()));
  }

  @PostMapping("/{budgetId}/note")
  public void updateBudgetNote(
      @PathVariable("budgetId") BudgetIdModel budgetIdModel,
      @RequestBody UpdateBudgetNoteModel updateBudgetNoteModel) {
    updateBudgetNoteUseCase.updateBudgetNote(
        budgetIdModel.getValue(), updateBudgetNoteModel.getNote());
  }
}
