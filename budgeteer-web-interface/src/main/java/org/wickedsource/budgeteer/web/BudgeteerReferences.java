package org.wickedsource.budgeteer.web;

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
        return new UrlResourceReference(Url.parse("/js/jquery/jquery.min.js"));
    }

    public static ResourceReference getHighchartsReference() {
        return new UrlResourceReference(Url.parse("/js/highcharts/highcharts.js"));
    }

}
