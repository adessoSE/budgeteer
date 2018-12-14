package de.adesso.budgeteer.web.pages.dashboard;

import de.adesso.budgeteer.web.BudgeteerSession;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class UsernameModel extends AbstractReadOnlyModel<String> {

    @Override
    public String getObject() {
        return BudgeteerSession.get().getLoggedInUser().getName();
    }
}
