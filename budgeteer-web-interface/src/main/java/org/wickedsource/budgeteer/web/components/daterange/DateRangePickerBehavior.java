package org.wickedsource.budgeteer.web.components.daterange;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.wickedsource.budgeteer.web.BudgeteerReferences;

import java.util.HashMap;
import java.util.Map;


public class DateRangePickerBehavior extends Behavior{
    private HashMap<String, String> options;

    /**
    * Implements the DateRangePicker
    * @param options @see <a href="https://github.com/dangrossman/bootstrap-daterangepicker#options">Options</a>
    */
    public DateRangePickerBehavior(HashMap<String, String> options){
        this.options = options;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getMomentJsReference()));
        // include daterangepicker.js
        ResourceReference jsResource = new PackageResourceReference(DateRangeInputField.class, "daterangepicker.js");
        response.render(JavaScriptReferenceHeaderItem.forReference(jsResource));
        // include css
        ResourceReference cssResource = new PackageResourceReference(DateRangeInputField.class, "daterangepicker-bs3.css");
        response.render(CssReferenceHeaderItem.forReference(cssResource));
        // activate daterangepicker on this input field
        component.setOutputMarkupId(true);
        response.render(OnDomReadyHeaderItem.forScript(getScript(component)));
    }

    public String getScript(Component component) {
        StringBuilder script = new StringBuilder("$('#"+component.getMarkupId()+"').daterangepicker(");
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
