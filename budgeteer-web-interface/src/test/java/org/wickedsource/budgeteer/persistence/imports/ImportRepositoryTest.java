package org.wickedsource.budgeteer.persistence.imports;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

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
