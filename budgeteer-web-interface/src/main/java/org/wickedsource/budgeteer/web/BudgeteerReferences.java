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
        return new UrlResourceReference(Url.parse("js/jquery/jquery.min.js")).setContextRelative(true);
    }

    public static ResourceReference getBootstrapMultiselectJSReference() {
        return new UrlResourceReference(Url.parse("js/plugins/bootstrap-multiselect/bootstrap.multiselect.js")).setContextRelative(true);
    }

    public static ResourceReference getBootstrapDataTableJSReference() {
        return new UrlResourceReference(Url.parse("js/plugins/datatables/dataTables.bootstrap.js")).setContextRelative(true);
    }

    public static ResourceReference getAdminLteAppReference() {
        return new UrlResourceReference(Url.parse("js/AdminLTE/app.js")).setContextRelative(true);
    }

    public static UrlResourceReference getBootstrapMultiselectCssReference() {
        return new UrlResourceReference(Url.parse("css/bootstrap-multiselect/bootstrap.multiselect.css")).setContextRelative(true);
    }

    public static ResourceReference getJQueryDataTableJSReference() {
        return new UrlResourceReference(Url.parse("js/plugins/datatables/jquery.dataTables.js")).setContextRelative(true);
    }

    public static ResourceReference getJQueryDataTableMomentSortJSReference() {
        return new UrlResourceReference(Url.parse("js/plugins/datatables/dataTable.sort.datetime-moment.js")).setContextRelative(true);
    }

    public static ResourceReference getJQueryDataTableDateRangeMomentSortJSReference() {
        return new UrlResourceReference(Url.parse("js/plugins/datatables/dataTable.sort.custom-moment.js")).setContextRelative(true);
    }

    public static ResourceReference getDataTableCssReference() {
        return new UrlResourceReference(Url.parse("css/datatables/dataTables.bootstrap.css")).setContextRelative(true);
    }

    public static ResourceReference getMomentJsReference() {
        return new UrlResourceReference(Url.parse("js/moment.js/moment.2.8.4.min.js")).setContextRelative(true);
    }

    public static ResourceReference getChartjsReference() {
        return new UrlResourceReference(Url.parse("js/chart.js/chartjs.2.7.2.min.js")).setContextRelative(true);
    }

    public static ResourceReference getManualTableSortingReference() {
        return new UrlResourceReference(Url.parse("js/manualTableSorting.js")).setContextRelative(true);
    }
}
