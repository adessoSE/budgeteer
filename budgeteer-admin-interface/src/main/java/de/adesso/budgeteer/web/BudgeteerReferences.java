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

    public static ResourceReference getJQueryReference() {
        return new UrlResourceReference(Url.parse("js/jquery/jquery.min.js")).setContextRelative(true);
    }

    public static ResourceReference getAdminLteAppReference() {
        return new UrlResourceReference(Url.parse("js/AdminLTE/app.js")).setContextRelative(true);
    }

    public static ResourceReference getMomentJsReference() {
        return new UrlResourceReference(Url.parse("js/moment.js/moment.2.8.4.min.js")).setContextRelative(true);
    }

    public static ResourceReference getChartjsReference() {
        return new UrlResourceReference(Url.parse("js/chart.js/chartjs.2.7.2.min.js")).setContextRelative(true);
    }

}
