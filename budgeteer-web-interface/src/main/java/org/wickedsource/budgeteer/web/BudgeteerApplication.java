package org.wickedsource.budgeteer.web;

import com.googlecode.wickedcharts.wicket6.JavaScriptResourceRegistry;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.web.usecase.dashboard.DashboardPage;

import java.util.Set;

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
        mountPages();
    }

    /**
     * Scans the classpath for all pages annotated with the Mount annotation and mounts them.
     */
    @SuppressWarnings("unchecked")
    private void mountPages() {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setUrls(
                        ClasspathHelper.forPackage("org.wickedsource.budgeteer")).setScanners(
                        new TypeAnnotationsScanner()));
        Set<Class<?>> pagesToMount = reflections.getTypesAnnotatedWith(Mount.class);

        for (Class<?> page : pagesToMount) {
            Class<? extends WebPage> pageClass = (Class<? extends WebPage>) page;
            Mount mount = pageClass.getAnnotation(Mount.class);
            for (String mountUrl : mount.value()) {
                mountPage(mountUrl, pageClass);
            }
        }
    }

    private void initWickedCharts() {
        JavaScriptResourceRegistry.getInstance().setHighchartsReference("js/highcharts/highcharts.js");
        JavaScriptResourceRegistry.getInstance().setJQueryReference("js/jquery/jquery.min.js");
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new BudgeteerSession(request);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        this.context = applicationContext;
    }

}


