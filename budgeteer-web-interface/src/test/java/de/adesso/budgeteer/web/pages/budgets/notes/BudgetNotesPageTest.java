package de.adesso.budgeteer.web.pages.budgets.notes;

import de.adesso.budgeteer.web.AbstractWebTestTemplate;
import org.junit.jupiter.api.Test;

public class BudgetNotesPageTest extends AbstractWebTestTemplate {

    @Test
    void render() {
        getTester().assertRenderedPage(BudgetNotesPage.class);
    }

    @Override
    protected void setupTest() {
        BudgetNotesPage page = new BudgetNotesPage();
        getTester().startPage(page);
    }
}
