package org.wickedsource.budgeteer.web.usecase.budgets.overview.table;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class ProgressBarTest extends AbstractWebTestTemplate {

    @Test
    public void render() {
        WicketTester tester = getTester();
        ProgressBar bar = new ProgressBar("bar", model(from(50d)));
        tester.startComponentInPage(bar);
    }
}
