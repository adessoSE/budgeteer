package de.adesso.budgeteer.web;

import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.UrlResourceReference;

/**
 * Collection of factory methods for ResourceReferences for Javascript files and the like.
 */
public class BudgeteerReferences {

    private BudgeteerReferences() {

    }

    private static ResourceReference jQueryReference = new UrlResourceReference(Url.parse("js/jquery/jquery.min.js")).setContextRelative(true);

    private static ResourceReference adminLteReference = new UrlResourceReference(Url.parse("js/AdminLTE/app.js")).setContextRelative(true);

    private static ResourceReference momentJsReference =  new UrlResourceReference(Url.parse("js/moment.js/moment.2.8.4.min.js")).setContextRelative(true);

    private static ResourceReference chartJsReference = new UrlResourceReference(Url.parse("js/chart.js/chartjs.2.7.2.min.js")).setContextRelative(true);

    public static ResourceReference getJQueryReference() {
        return jQueryReference;
    }

    public static ResourceReference getAdminLteAppReference() {
        return adminLteReference;
    }

    public static ResourceReference getMomentJsReference() {
        return momentJsReference;
    }

    public static ResourceReference getChartjsReference() {
        return chartJsReference;
    }

}
