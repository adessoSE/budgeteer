package org.wickedsource.budgeteer.web.base;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.web.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.people.PeopleOverviewPage;

@Component
public class BudgeteerApplication extends WebApplication implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public Class<? extends WebPage> getHomePage() {
        return DashboardPage.class;
    }

    @Override
    public void init() {
        super.init();

        getMarkupSettings().setStripWicketTags(true);
        getComponentInstantiationListeners().add(new SpringComponentInjector(this, context));

        mountPage("dashboard", DashboardPage.class);
        mountPage("people", PeopleOverviewPage.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
