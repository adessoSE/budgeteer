package org.wickedsource.budgeteer.web.component.hourstable;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

public class FilterPanelTest extends AbstractWebTestTemplate{

    @Test
    public void render(){
        WicketTester tester = getTester();
        FilterPanel panel = new FilterPanel("panel");
        tester.startComponentInPage(panel);
    }
}
