package de.adesso.budgeteer.persistence.imports;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import de.adesso.budgeteer.persistence.DbUnitDataJpaTestBase;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ImportRepositoryTest extends DbUnitDataJpaTestBase {

  @Autowired private ImportRepository repository;

  @Test
  @DatabaseSetup("findByProjectId.xml")
  @DatabaseTearDown(value = "findByProjectId.xml", type = DatabaseOperation.DELETE_ALL)
  void testFindByProjectId() {
    List<ImportEntity> imports = repository.findByProjectId(1L);
    Assertions.assertEquals(2, imports.size());
  }
}
