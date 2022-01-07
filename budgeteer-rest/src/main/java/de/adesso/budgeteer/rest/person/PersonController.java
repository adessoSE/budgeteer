package de.adesso.budgeteer.rest.person;

import de.adesso.budgeteer.core.person.port.in.*;
import de.adesso.budgeteer.rest.budget.model.BudgetIdModel;
import de.adesso.budgeteer.rest.person.model.CreatePersonModel;
import de.adesso.budgeteer.rest.person.model.PersonIdModel;
import de.adesso.budgeteer.rest.person.model.PersonModel;
import de.adesso.budgeteer.rest.person.model.PersonWithRatesModel;
import de.adesso.budgeteer.rest.project.model.ProjectIdModel;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/people")
public class PersonController {
  private final GetPeopleInBudgetUseCase getPeopleInBudgetUseCase;
  private final GetPeopleInProjectUseCase getPeopleInProjectUseCase;
  private final GetPersonByIdUseCase getPersonByIdUseCase;
  private final GetPersonWithRatesByPersonIdUseCase getPersonWithRatesByPersonIdUseCase;
  private final DeletePersonByIdUseCase deletePersonByIdUseCase;
  private final CreatePersonUseCase createPersonUseCase;
  private final UpdatePersonUseCase updatePersonUseCase;
  private final PersonModelMapper personModelMapper;

  @GetMapping("/inBudget/{budgetId}")
  @PreAuthorize("userHasAccessToBudget(#budgetIdModel.value)")
  public List<PersonModel> getPeopleInBudget(
      @PathVariable("budgetId") BudgetIdModel budgetIdModel) {
    return personModelMapper.toPersonModel(
        getPeopleInBudgetUseCase.getPeopleInBudget(budgetIdModel.getValue()));
  }

  @GetMapping("/inProject/{projectId}")
  @PreAuthorize("userHasAccessToProject(#projectIdModel.value)")
  public List<PersonModel> getPeopleInProject(
      @PathVariable("projectId") ProjectIdModel projectIdModel) {
    return personModelMapper.toPersonModel(
        getPeopleInProjectUseCase.getPeopleInProject(projectIdModel.getValue()));
  }

  @GetMapping("/{personId}")
  @PreAuthorize("userHasAccessToPerson(#personIdModel.value)")
  public Optional<PersonModel> getPerson(@PathVariable("personId") ProjectIdModel personIdModel) {
    return getPersonByIdUseCase
        .getPersonById(personIdModel.getValue())
        .map(personModelMapper::toPersonModel);
  }

  @GetMapping("/withRates/{personId}")
  @PreAuthorize("userHasAccessToPerson(#personIdModel.value)")
  public Optional<PersonWithRatesModel> getPersonWithRates(
      @PathVariable("personId") PersonIdModel personIdModel) {
    return Optional.ofNullable(
        personModelMapper.toPersonWithRatesModel(
            getPersonWithRatesByPersonIdUseCase.getPersonWithRatesByPersonId(
                personIdModel.getValue())));
  }

  @DeleteMapping("/{personId}")
  @PreAuthorize("userHasAccessToPerson(#personIdModel.value)")
  public void deletePerson(@PathVariable("personId") PersonIdModel personIdModel) {
    deletePersonByIdUseCase.deletePersonById(personIdModel.getValue());
  }

  @PostMapping
  @PreAuthorize("userHasAccessToProject(#createPersonModel.projectId)")
  public PersonModel createPerson(@Valid @RequestBody CreatePersonModel createPersonModel) {
    return personModelMapper.toPersonModel(
        createPersonUseCase.createPerson(
            new CreatePersonUseCase.CreatePersonCommand(
                createPersonModel.getPersonName(),
                createPersonModel.getImportKey(),
                createPersonModel.getDefaultDailyRate(),
                createPersonModel.getProjectId(),
                createPersonModel.getRates())));
  }

  @PutMapping("/{personId}")
  @PreAuthorize("userHasAccessToPerson(#personIdModel.value)")
  public PersonModel updatePerson(
      @PathVariable("personId") PersonIdModel personIdModel,
      @Valid @RequestBody CreatePersonModel createPersonModel) {
    return personModelMapper.toPersonModel(
        updatePersonUseCase.updatePerson(
            new UpdatePersonUseCase.UpdatePersonCommand(
                personIdModel.getValue(),
                createPersonModel.getPersonName(),
                createPersonModel.getImportKey(),
                createPersonModel.getDefaultDailyRate(),
                createPersonModel.getRates())));
  }
}
