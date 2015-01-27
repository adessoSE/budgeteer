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

    public static ResourceReference getBootstrapMultiselectJSReference() {
        return new UrlResourceReference(Url.parse("/js/plugins/bootstrap-multiselect/bootstrap.multiselect.js"));
    }

    public static ResourceReference getBootstrapDataTableJSReference() {
        return new UrlResourceReference(Url.parse("/js/plugins/datatables/dataTables.bootstrap.js"));
    }

    public static ResourceReference getAdminLteAppReference() {
        return new UrlResourceReference(Url.parse("/js/AdminLTE/app.js"));
    }

    public static ResourceReference getBootstrapMultiselectCssReference() {
        return new UrlResourceReference(Url.parse("/css/bootstrap-multiselect/bootstrap.multiselect.css")) ;
    }


    public static ResourceReference getJQueryDataTableJSReference() {
        return new UrlResourceReference(Url.parse("/js/plugins/datatables/jquery.dataTables.js"));
    }

    public static ResourceReference getDataTableCssReference() {
        return new UrlResourceReference(Url.parse("/css/datatables/dataTables.bootstrap.css"));
    }

}
