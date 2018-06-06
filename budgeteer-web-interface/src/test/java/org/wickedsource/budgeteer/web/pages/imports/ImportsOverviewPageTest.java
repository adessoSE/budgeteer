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

    @Test
    void render() {
        WicketTester tester = getTester();
        tester.startPage(ImportsOverviewPage.class);
        tester.assertRenderedPage(ImportsOverviewPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
