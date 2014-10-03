package org.wickedsource.budgeteer.web.usecase.base;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

public class BudgeteerSession extends WebSession {

    public BudgeteerSession(Request request) {
        super(request);
    }

    public long getLoggedInUserId() {
        return 1l;
    }

    public static BudgeteerSession get() {
        return (BudgeteerSession) WebSession.get();
    }

}
