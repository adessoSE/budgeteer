package de.adesso.budgeteer.web.pages.budgets.notes;

import org.junit.jupiter.api.Test;
import de.adesso.budgeteer.web.AbstractWebTestTemplate;

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
