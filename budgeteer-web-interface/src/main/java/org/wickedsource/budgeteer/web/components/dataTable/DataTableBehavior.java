package org.wickedsource.budgeteer.web.components.dataTable;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.wickedsource.budgeteer.web.BudgeteerReferences;

import java.util.HashMap;
import java.util.Map;

public class DataTableBehavior extends Behavior{
    private HashMap<String, String> options;


    /**
     * Implements the AdminLTE-DataTable
     * @param options @see <a href="https://github.com/almasaeed2010/AdminLTE/blob/master/pages/tables/data.html">Options</a>
     */
    public DataTableBehavior(HashMap<String, String> options){
        this.options = options;
    }


    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(BudgeteerReferences.getDataTableCssReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryDataTableJSReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getBootstrapDataTableJSReference()));

        component.setOutputMarkupId(true);
        response.render(OnDomReadyHeaderItem.forScript(getScript(component)));

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
     * @return
     */
    public static HashMap<String, String> getRecommendedOptions(){
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("bPaginate", "true");
        result.put("bLengthChange", "false");
        result.put("bFilter", "false");
        result.put("bSort", "true");
        result.put("bInfo", "true");
        return result;
    }
}
