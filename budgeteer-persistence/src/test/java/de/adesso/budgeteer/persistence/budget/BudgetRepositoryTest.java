package de.adesso.budgeteer.persistence.budget;

import static org.assertj.core.api.Assertions.assertThat;

import de.adesso.budgeteer.persistence.DataJpaTestBase;
import de.adesso.budgeteer.persistence.contract.ContractEntity;
import de.adesso.budgeteer.persistence.project.ProjectEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BudgetRepositoryTest extends DataJpaTestBase {

  @Autowired private BudgetRepository budgetRepository;

  private static final Consumer<ProjectEntity> DEFAULT_PROJECT =
      projectEntity -> projectEntity.setName("project");

  private static final Consumer<ContractEntity> DEFAULT_CONTRACT =
      contractEntity -> {
        contractEntity.setName("contract");
        contractEntity.setTaxRate(BigDecimal.ZERO);
      };
  private static final Consumer<BudgetEntity> DEFAULT_BUDGET =
      budgetEntity -> {
        budgetEntity.setName("budget");
        budgetEntity.setImportKey("importKey");
      };
  private static final Consumer<BudgetEntity> ALTERNATIVE_BUDGET =
      budgetEntity -> {
        budgetEntity.setName("alternative-budget");
        budgetEntity.setImportKey("alternative-importKey");
      };

  private Consumer<BudgetEntity> setProject(ProjectEntity projectEntity) {
    return budgetEntity -> budgetEntity.setProject(projectEntity);
  }

  private Consumer<BudgetEntity> setContract(ContractEntity contractEntity) {
    return budgetEntity -> budgetEntity.setContract(contractEntity);
  }

  @Test
  void shouldRemoveReferencesToContract() {
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    var contract =
        persistEntity(
            new ContractEntity(),
            DEFAULT_CONTRACT.andThen(contractEntity -> contractEntity.setProject(project)));
    var budgets =
        List.of(
            persistEntity(
                new BudgetEntity(),
                DEFAULT_BUDGET.andThen(setProject(project)).andThen(setContract(contract))),
            persistEntity(
                new BudgetEntity(),
                ALTERNATIVE_BUDGET.andThen(setProject(project)).andThen(setContract(contract))));

    budgetRepository.removeReferencesToContract(contract.getId());
    entityManager.clear();

    var updatedBudgets =
        budgets.stream().map(budget -> entityManager.find(BudgetEntity.class, budget.getId()));
    assertThat(updatedBudgets).extracting(BudgetEntity::getContract).containsOnlyNulls();
  }

  @Test
  void shouldReturnAllTags() {
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    var budget = persistEntity(new BudgetEntity(), DEFAULT_BUDGET.andThen(setProject(project)));
    var budgetTags =
        List.of(
            persistEntity(
                new BudgetTagEntity(),
                budgetTagEntity -> {
                  budgetTagEntity.setBudget(budget);
                  budgetTagEntity.setTag("tag1");
                }),
            persistEntity(
                new BudgetTagEntity(),
                budgetTagEntity -> {
                  budgetTagEntity.setBudget(budget);
                  budgetTagEntity.setTag("tag2");
                }));
    persistEntity(budget, budgetEntity -> budgetEntity.getTags().addAll(budgetTags));

    var foundTags = budgetRepository.getAllTagsInProject(project.getId());

    assertThat(foundTags)
        .containsAll(budgetTags.stream().map(BudgetTagEntity::getTag).collect(Collectors.toList()));
  }

  @Test
  void shouldReturnBudgetNote() {
    var expected = "note";
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    var budget =
        persistEntity(
            new BudgetEntity(),
            DEFAULT_BUDGET
                .andThen(setProject(project))
                .andThen(budgetEntity -> budgetEntity.setNote(expected)));

    var note = budgetRepository.getNoteForBudget(budget.getId());

    assertThat(note).isEqualTo(expected);
  }

  @Test
  void shouldUpdateBudgetNote() {
    var newNote = "new-note";
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    var budget =
        persistEntity(
            new BudgetEntity(),
            DEFAULT_BUDGET
                .andThen(setProject(project))
                .andThen(budgetEntity -> budgetEntity.setNote(newNote)));

    budgetRepository.updateNoteForBudget(budget.getId(), newNote);
    entityManager.clear();

    var foundBudget = entityManager.find(BudgetEntity.class, budget.getId());
    assertThat(foundBudget.getNote()).isEqualTo(newNote);
  }

  @Test
  void shouldReturnTrueIfBudgetWithNameExistsInProject() {
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    var budget1 = persistEntity(new BudgetEntity(), DEFAULT_BUDGET.andThen(setProject(project)));
    var budget2 =
        persistEntity(new BudgetEntity(), ALTERNATIVE_BUDGET.andThen(setProject(project)));

    var returned =
        budgetRepository.existsWithNameInProjectByBudgetId(budget2.getId(), budget1.getName());

    assertThat(returned).isTrue();
  }

  @Test
  void shouldReturnFalseIfBudgetWithNameExitsInDifferentProject() {
    var project1 = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    var project2 =
        persistEntity(new ProjectEntity(), projectEntity -> projectEntity.setName("alt-project"));
    var budget1 = persistEntity(new BudgetEntity(), DEFAULT_BUDGET.andThen(setProject(project1)));
    var budget2 =
        persistEntity(new BudgetEntity(), ALTERNATIVE_BUDGET.andThen(setProject(project2)));

    var returned =
        budgetRepository.existsWithNameInProjectByBudgetId(budget2.getId(), budget1.getName());

    assertThat(returned).isFalse();
  }

  @Test
  void shouldReturnFalseIfNoBudgetWithNameExitsInProject() {
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    persistEntity(new BudgetEntity(), DEFAULT_BUDGET.andThen(setProject(project)));
    var budget2 =
        persistEntity(new BudgetEntity(), ALTERNATIVE_BUDGET.andThen(setProject(project)));

    var returned =
        budgetRepository.existsWithNameInProjectByBudgetId(budget2.getId(), "a-different-name");

    assertThat(returned).isFalse();
  }

  @Test
  void shouldReturnTrueIfBudgetWithImportKeyExistsInProject() {
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    var budget1 = persistEntity(new BudgetEntity(), DEFAULT_BUDGET.andThen(setProject(project)));
    var budget2 =
        persistEntity(new BudgetEntity(), ALTERNATIVE_BUDGET.andThen(setProject(project)));

    var returned =
        budgetRepository.existsWithImportKeyInProjectByBudgetId(
            budget2.getId(), budget1.getImportKey());

    assertThat(returned).isTrue();
  }

  @Test
  void shouldReturnFalseIfBudgetWithImportKeyExitsInDifferentProject() {
    var project1 = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    var project2 =
        persistEntity(new ProjectEntity(), projectEntity -> projectEntity.setName("alt-project"));
    var budget1 = persistEntity(new BudgetEntity(), DEFAULT_BUDGET.andThen(setProject(project1)));
    var budget2 =
        persistEntity(new BudgetEntity(), ALTERNATIVE_BUDGET.andThen(setProject(project2)));

    var returned =
        budgetRepository.existsWithImportKeyInProjectByBudgetId(
            budget2.getId(), budget1.getImportKey());

    assertThat(returned).isFalse();
  }

  @Test
  void shouldReturnFalseIfNoBudgetWithImportKeyExitsInProject() {
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    persistEntity(new BudgetEntity(), DEFAULT_BUDGET.andThen(setProject(project)));
    var budget2 =
        persistEntity(new BudgetEntity(), ALTERNATIVE_BUDGET.andThen(setProject(project)));

    var returned =
        budgetRepository.existsWithImportKeyInProjectByBudgetId(
            budget2.getId(), "a-different-importKey");

    assertThat(returned).isFalse();
  }
}
