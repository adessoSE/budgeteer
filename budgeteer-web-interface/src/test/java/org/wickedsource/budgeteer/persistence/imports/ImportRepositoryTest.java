package org.wickedsource.budgeteer.persistence.imports;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;

import java.util.List;

class ImportRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    private ImportRepository repository;

    @Test
    @DatabaseSetup("findByProjectId.xml")
    @DatabaseTearDown(value = "findByProjectId.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindByProjectId() {
        List<ImportEntity> imports = repository.findByProjectId(1L);
        Assertions.assertEquals(2, imports.size());
    }
}
