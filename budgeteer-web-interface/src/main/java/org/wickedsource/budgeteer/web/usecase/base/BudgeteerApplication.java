package org.wickedsource.budgeteer.web.usecase.base;

import com.googlecode.wickedcharts.wicket6.JavaScriptResourceRegistry;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.usecase.people.PeopleOverviewPage;

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
        initWickedCharts();

        mountPage("dashboard", DashboardPage.class);
        mountPage("people", PeopleOverviewPage.class);
    }

    private void initWickedCharts(){
        JavaScriptResourceRegistry.getInstance().setHighchartsReference("js/highcharts/highcharts.js");
        JavaScriptResourceRegistry.getInstance().setJQueryReference("js/jquery/jquery.min.js");
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new BudgeteerSession(request);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}


