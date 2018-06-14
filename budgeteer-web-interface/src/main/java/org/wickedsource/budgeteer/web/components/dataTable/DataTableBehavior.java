package org.wickedsource.budgeteer.web.components.dataTable;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.wickedsource.budgeteer.web.BudgeteerReferences;
import org.wickedsource.budgeteer.web.BudgeteerSession;

import java.util.HashMap;
import java.util.Map;

public class DataTableBehavior extends Behavior{
    private HashMap<String, String> options;


    /**
     * Implements the JQuery-DataTable
     * @param options @see <a href="http://www.datatables.net/reference/option/">Options</a>
     */
    public DataTableBehavior(HashMap<String, String> options){
        this.options = options;
    }


    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(BudgeteerReferences.getDataTableCssReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryDataTableJSReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getMomentJsReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryDataTableMomentSortJSReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getBootstrapDataTableJSReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryDataTableDateRangeMomentSortJSReference()));

        component.setOutputMarkupId(true);
        response.render(OnDomReadyHeaderItem.forScript(getSorting()));
        response.render(OnDomReadyHeaderItem.forScript(getScript(component)));

    }

    private String getSorting() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("$.fn.dataTable.moment('D.MM.YY');");
        stringBuilder.append("$.fn.dataTable.moment('YYYY/MM');");
        stringBuilder.append(getCustomSorting());
        return stringBuilder.toString();
    }

    private String getCustomSorting() {
        return "$.fn.dataTable.ext.type.detect.unshift(\n" +
                "    function ( d ) {\n" +
                "        var t = $.parseHTML(d); return $(t).find('.wicketBehaviorclick').length > 0 ?\n" +
                "            'custom-Money-Label' :\n" +
                "            null;\n" +
                "    }\n" +
                ");\n" +
                " \n" +
                "$.fn.dataTable.ext.type.order['custom-Money-Label-pre'] = function ( d ) {\n" +
                " var t = $.parseHTML(d); " +
                " var text = $(t).find('.wicketBehaviorclick').text().replace(/\\D/g, ''); " +
                " var postFix= $(t).nextAll().length > 0 ? '00000' :''; " +
                " return parseInt(text+postFix);\n" +
                "};";
    }

    public String getScript(Component component) {
        StringBuilder script = new StringBuilder("$('#"+component.getMarkupId()+"').dataTable(");
        if(options != null && ! options.isEmpty()){
            script.append("{");
            int index = 1;
            for(Map.Entry<String, String> entry : options.entrySet()) {
                script.append(entry.getKey() + ": " + entry.getValue());
                if(index < options.size()){
                    script.append(",");
                }
                index++;
            }
            script.append("}");
        }
        script.append(");");
        return script.toString();
    }

    /**
     * Returns a HashMap with the mostly used options set. E.g. Pagination oder sortable columns are enabled.
     * @return a map containing the recommended options.
     */
    public static HashMap<String, String> getRecommendedOptions(){
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("paging", "true");
        result.put("lengthChange", "false");
        result.put("ordering", "true");
        result.put("info", "true");
        if(BudgeteerSession.get().getLocale().getLanguage().equals("de")) {
            result.put("language", String.format("{decimal: \"%s\", thousands: \"%s\"}", ",", "."));
        }
        return result;
    }
}
