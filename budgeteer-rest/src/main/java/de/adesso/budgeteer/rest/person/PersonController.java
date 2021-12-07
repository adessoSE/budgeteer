package de.adesso.budgeteer.rest.person;

import de.adesso.budgeteer.core.person.port.in.*;
import de.adesso.budgeteer.rest.person.model.CreatePersonModel;
import de.adesso.budgeteer.rest.person.model.PersonModel;
import de.adesso.budgeteer.rest.person.model.PersonWithRatesModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    @PreAuthorize("userHasAccessToBudget(#budgetId)")
    public List<PersonModel> getPeopleInBudget(@PathVariable long budgetId) {
        return personModelMapper.toPersonModel(getPeopleInBudgetUseCase.getPeopleInBudget(budgetId));
    }

    @GetMapping("/inProject/{projectId}")
    @PreAuthorize("userHasAccessToProject(#projectId)")
    public List<PersonModel> getPeopleInProject(@PathVariable long projectId) {
        return personModelMapper.toPersonModel(getPeopleInProjectUseCase.getPeopleInProject(projectId));
    }

    @GetMapping("/{personId}")
    @PreAuthorize("userHasAccessToPerson(#personId)")
    public Optional<PersonModel> getPerson(@PathVariable long personId) {
        return getPersonByIdUseCase.getPersonById(personId).map(personModelMapper::toPersonModel);
    }

    @GetMapping("/withRates/{personId}")
    @PreAuthorize("userHasAccessToPerson(#personId)")
    public Optional<PersonWithRatesModel> getPersonWithRates(@PathVariable long personId) {
        return Optional.ofNullable( personModelMapper.toPersonWithRatesModel(getPersonWithRatesByPersonIdUseCase.getPersonWithRatesByPersonId(personId)));
    }

    @DeleteMapping("/{personId}")
    @PreAuthorize("userHasAccessToPerson(#personId)")
    public void deletePerson(@PathVariable long personId) {
        deletePersonByIdUseCase.deletePersonById(personId);
    }

    @PostMapping
    @PreAuthorize("userHasAccessToProject(#createPersonModel.projectId)")
    public PersonModel createPerson(@Valid @RequestBody CreatePersonModel createPersonModel) {
        return personModelMapper.toPersonModel(createPersonUseCase.createPerson(new CreatePersonUseCase.CreatePersonCommand(
                createPersonModel.getPersonName(),
                createPersonModel.getImportKey(),
                createPersonModel.getDefaultDailyRate(),
                createPersonModel.getProjectId(),
                createPersonModel.getRates()
        )));
    }

    @PutMapping("/{personId}")
    @PreAuthorize("userHasAccessToPerson(#personId)")
    public PersonModel updatePerson(@Valid @RequestBody CreatePersonModel createPersonModel, @PathVariable long personId) {
        return personModelMapper.toPersonModel(updatePersonUseCase.updatePerson(new UpdatePersonUseCase.UpdatePersonCommand(
                personId,
                createPersonModel.getPersonName(),
                createPersonModel.getImportKey(),
                createPersonModel.getDefaultDailyRate(),
                createPersonModel.getRates()
        )));
    }
}
