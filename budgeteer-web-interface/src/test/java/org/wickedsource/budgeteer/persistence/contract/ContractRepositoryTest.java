package org.wickedsource.budgeteer.persistence.contract;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.DataJpaTestBase;
import org.wickedsource.budgeteer.persistence.project.ProjectContractField;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class ContractRepositoryTest extends DataJpaTestBase {

    @Autowired private ContractRepository contractRepository;

    private static final Consumer<ProjectEntity> DEFAULT_PROJECT = projectEntity -> projectEntity.setName("project");

    private static final Consumer<ContractEntity> DEFAULT_CONTRACT = contractEntity -> {
        contractEntity.setName("name");
        contractEntity.setTaxRate(BigDecimal.valueOf(19L));
    };

    @Test
    void shouldDeleteContractByProjectId() {
        var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
        var contract = persistEntity(new ContractEntity(), DEFAULT_CONTRACT.andThen(contractEntity -> contractEntity.setProject(project)));

        contractRepository.deleteByProjectId(project.getId());
        entityManager.clear();

        var foundContract = entityManager.find(ContractEntity.class, contract.getId());
        assertThat(foundContract).isNull();
    }

    @Test
    void shouldDeleteAllContractFieldsWithProjectId() {
        var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
        var projectContractField1 = persistEntity(new ProjectContractField("field1", project), a -> {});
        var projectContractField2 = persistEntity(new ProjectContractField("field2", project), a -> {});
        var contractFields = List.of(
                persistEntity(new ContractFieldEntity(projectContractField1, "value1"), a -> {}),
                persistEntity(new ContractFieldEntity(projectContractField2, "value2"), a -> {})
        );
        var contract = persistEntity(new ContractEntity(), DEFAULT_CONTRACT.andThen(contractEntity -> {
            contractEntity.setProject(project);
            contractEntity.setContractFields(contractFields);
        }));

        contractRepository.deleteContractFieldByProjectId(project.getId());
        entityManager.clear();

        var updatedContract = entityManager.find(ContractEntity.class, contract.getId());
        assertThat(updatedContract.getContractFields()).isEmpty();
    }
}
