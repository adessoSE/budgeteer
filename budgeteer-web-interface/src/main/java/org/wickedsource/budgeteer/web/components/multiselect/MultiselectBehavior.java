package org.wickedsource.budgeteer.web.components.multiselect;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse;
import org.apache.wicket.request.Response;
import org.wickedsource.budgeteer.web.BudgeteerReferences;

import java.util.HashMap;
import java.util.Map;

public class MultiselectBehavior extends Behavior{
    private HashMap<String, String> options;

    /**
     * Sets the options which are used to initialize the multiselect.
     * @param options @see <a href="http://davidstutz.github.io/bootstrap-multiselect/#configuration-options">Options</a>
     */
    public MultiselectBehavior(HashMap<String, String> options){
        this.options = options;
    }


    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(BudgeteerReferences.getBootstrapMultiselectCssReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getBootstrapMultiselectJSReference()));

        component.setOutputMarkupId(true);
        response.render(OnDomReadyHeaderItem.forScript(getScript(component)));

    }

    public String getScript(Component component) {
        StringBuilder script = new StringBuilder("$('#"+component.getMarkupId()+"').multiselect(");
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
}
