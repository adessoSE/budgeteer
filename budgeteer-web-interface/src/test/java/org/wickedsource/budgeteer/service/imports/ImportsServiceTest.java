package org.wickedsource.budgeteer.service.imports;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportRepository;
import org.wickedsource.budgeteer.persistence.record.RecordRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class ImportsServiceTest extends ServiceTestTemplate {

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private ImportsService importService;

    @Test
    public void testLoadImports() throws Exception {
        when(importRepository.findByProjectId(1l)).thenReturn(Arrays.asList(createImportEntity()));
        List<Import> imports = importService.loadImports(1l);
        Assert.assertEquals(1, imports.size());
        Assert.assertEquals("TestImport", imports.get(0).getImportType());
    }

    @Test
    public void testDeleteImport() throws Exception {
        importService.deleteImport(1l);
        verify(importRepository, times(1)).delete(1l);
        verify(recordRepository, times(1)).deleteByImport(1l);
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
