package de.adesso.budgeteer.persistence.contract;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import de.adesso.budgeteer.core.contract.domain.Contract;
import de.adesso.budgeteer.core.contract.port.out.UpdateContractEntityPort;
import de.adesso.budgeteer.persistence.budget.BudgetRepository;
import de.adesso.budgeteer.persistence.project.ProjectContractField;
import de.adesso.budgeteer.persistence.project.ProjectEntity;
import de.adesso.budgeteer.persistence.project.ProjectRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContractAdapterTest {

  @InjectMocks private ContractAdapter contractAdapter;
  @Mock private ContractRepository contractRepository;
  @Mock private ProjectRepository projectRepository;
  @Mock private BudgetRepository budgetRepository;
  @Mock private ContractMapper contractMapper;

  @Test
  void shouldUpdateContractEntity() {
    var project = new ProjectEntity();
    project.setId(5L);
    var originalContract = new ContractEntity();
    var contractFields =
        new ArrayList<>(
            List.of(
                new ContractFieldEntity(new ProjectContractField("field1", project), "value1")));
    originalContract.setId(1L);
    originalContract.setName("name");
    originalContract.setInternalNumber("internalNumber");
    originalContract.setProject(project);
    originalContract.setStartDate(LocalDate.of(2020, 2, 22));
    originalContract.setType(ContractEntity.ContractType.FIXED_PRICE);
    originalContract.setBudget(Money.of(CurrencyUnit.EUR, 1000L));
    originalContract.setTaxRate(BigDecimal.valueOf(19L));
    originalContract.setContractFields(contractFields);
    originalContract.setLink(null);
    originalContract.setFileName(null);
    originalContract.setFile(null);

    var attributes =
        originalContract.getContractFields().stream()
            .collect(
                Collectors.toMap(a -> a.getField().getFieldName(), b -> "new-" + b.getValue()));
    attributes.put("field2", "new-value2");
    var command =
        new UpdateContractEntityPort.UpdateContractEntityCommand(
            originalContract.getId(),
            "new-name",
            "new-internalNumber",
            LocalDate.of(2021, 2, 22),
            Contract.Type.TIME_AND_MATERIAL,
            Money.of(CurrencyUnit.EUR, 1500L),
            BigDecimal.valueOf(16L),
            attributes,
            "link",
            "fileName",
            new byte[] {0, 1, 2, 3, 4});

    var expectedUpdatedEntity = new ContractEntity();
    expectedUpdatedEntity.setId(originalContract.getId());
    expectedUpdatedEntity.setName(command.getName());
    expectedUpdatedEntity.setInternalNumber(command.getInternalNumber());
    expectedUpdatedEntity.setProject(project);
    expectedUpdatedEntity.setStartDate(command.getStartDate());
    expectedUpdatedEntity.setType(ContractEntity.ContractType.T_UND_M);
    expectedUpdatedEntity.setBudget(command.getBudget());
    expectedUpdatedEntity.setTaxRate(command.getTaxRate());
    expectedUpdatedEntity.setContractFields(
        List.of(
            new ContractFieldEntity(new ProjectContractField("field1", project), "new-value1"),
            new ContractFieldEntity(new ProjectContractField("field2", project), "new-value2")));
    expectedUpdatedEntity.setLink(command.getLink());
    expectedUpdatedEntity.setFileName(command.getFileName());
    expectedUpdatedEntity.setFile(command.getFile());

    when(contractRepository.findById(originalContract.getId()))
        .thenReturn(Optional.of(originalContract));
    when(contractRepository.save(any())).thenReturn(null);

    contractAdapter.updateContractEntity(command);

    ArgumentCaptor<ContractEntity> captor = ArgumentCaptor.forClass(ContractEntity.class);
    verify(contractRepository).save(captor.capture());
    assertThat(captor.getValue()).isEqualTo(expectedUpdatedEntity);
  }
}
