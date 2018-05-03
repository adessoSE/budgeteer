package org.wickedsource.budgeteer.service.imports;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class ImportServiceTest extends ServiceTestTemplate {

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private ImportService importService;

    @Test
    public void testLoadImports() throws Exception {
        when(importRepository.findByProjectId(1l)).thenReturn(Arrays.asList(createImportEntity()));
        List<Import> imports = importService.loadImports(1l);
        Assertions.assertEquals(1, imports.size());
        Assertions.assertEquals("TestImport", imports.get(0).getImportType());
    }

    @Test
    public void testDeleteImport() throws Exception {
        importService.deleteImport(1l);
        verify(importRepository, times(1)).delete(1l);
        verify(workRecordRepository, times(1)).deleteByImport(1l);
    }

    private ImportEntity createImportEntity() {
        ImportEntity entity = new ImportEntity();
        entity.setEndDate(new Date());
        entity.setStartDate(new Date());
        entity.setImportType("TestImport");
        entity.setId(1l);
        return entity;
    }

}
