package org.wickedsource.budgeteer.web.components.taxLabel;

import org.apache.wicket.markup.html.basic.Label;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.BudgeteerSession;


public class TaxModelTest extends AbstractWebTestTemplate {

    private TaxLabelModel testSubject;
    private Label label;

    @Override
    protected void setupTest() {
        testSubject = new TaxLabelModel("test");
        label = new Label("hallo", testSubject);
        getTester().startComponentInPage(label);
    }

    @Test
    public void testNetText() {
        BudgeteerSession.get().setTaxEnabled(false);
        getTester().assertLabel("hallo", "test (net)");
    }

    @Test
    public void testGrossText() {
        BudgeteerSession.get().setTaxEnabled(true);
//        getTester().debugComponentTrees();
//        getTester().dumpPage();0
        getTester().assertLabel("hallo", "test (gross)");
    }
}