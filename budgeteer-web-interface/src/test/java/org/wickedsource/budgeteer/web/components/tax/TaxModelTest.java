package org.wickedsource.budgeteer.web.components.tax;

import org.apache.wicket.markup.html.basic.Label;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.BudgeteerSession;


public class TaxModelTest extends AbstractWebTestTemplate {

    @Override
    protected void setupTest() {
        TaxLabelModel testSubject = new TaxLabelModel("test");
        Label label = new Label("hallo", testSubject);
        getTester().startComponentInPage(label);
    }

    @Test
    void testNetText() {
        BudgeteerSession.get().setTaxEnabled(false);
        getTester().assertLabel("hallo", "test (net)");
    }

    @Test
    void testGrossText() {
        BudgeteerSession.get().setTaxEnabled(true);
//        getTester().debugComponentTrees();
//        getTester().dumpPage();0
        getTester().assertLabel("hallo", "test (gross)");
    }
}