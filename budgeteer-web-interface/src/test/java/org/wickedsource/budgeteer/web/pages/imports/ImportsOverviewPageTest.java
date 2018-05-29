package org.wickedsource.budgeteer.web.pages.imports;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.imports.Import;
import org.wickedsource.budgeteer.service.imports.ImportService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

public class ImportsOverviewPageTest extends AbstractWebTestTemplate {

    @Autowired
    private ImportService service;

    @Test
    void render() {
        WicketTester tester = getTester();
        when(service.loadImports(1L)).thenReturn(createImports());
        tester.startPage(ImportsOverviewPage.class);
        tester.assertRenderedPage(ImportsOverviewPage.class);
    }

    private List<Import> createImports() {
        List<Import> list = new ArrayList<Import>();
        for (int i = 0; i < 20; i++) {
            Import importRecord = new Import();
            importRecord.setEndDate(new Date());
            importRecord.setStartDate(new Date());
            importRecord.setImportDate(new Date());
            importRecord.setImportType("aproda import");
            list.add(importRecord);
        }
        return list;
    }

    @Override
    protected void setupTest() {

    }
}
