package org.wickedsource.budgeteer.web.pages.budgets.edit.tagsfield;

import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.convert.IConverter;
import org.wickedsource.budgeteer.web.BudgeteerReferences;

import java.util.List;

public class TagsTextField extends TextField<List<String>> {

    public TagsTextField(String id, IModel<List<String>> model) {
        super(id, model);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <List> IConverter<List> getConverter(Class<List> type) {
        return (IConverter<List>) new TagsConverter();
    }

    @Override
    protected String getInputType() {
        return "text";
    }

    @Override
    public void renderHead(HtmlHeaderContainer container) {
        // jquery resource
        container.getHeaderResponse().render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryReference()));
        // include javascript
        ResourceReference jsResource = new PackageResourceReference(TagsTextField.class, "bootstrap-tagsinput.min.js");
        container.getHeaderResponse().render(JavaScriptReferenceHeaderItem.forReference(jsResource));
        // include css
        ResourceReference cssResource = new PackageResourceReference(TagsTextField.class, "bootstrap-tagsinput.css");
        container.getHeaderResponse().render(CssReferenceHeaderItem.forReference(cssResource));
        // activate tagsinput on this input field
        container.getHeaderResponse().render(JavaScriptHeaderItem.forScript(String.format("window.onload = function () {\n" +
                "     $('#%s').tagsinput({\n" +
                "            tagClass: function (item) {\n" +
                "                return 'badge bg-light-blue';\n" +
                "            }\n" +
                "        });\n" +
                "}", getMarkupId()), "activate-tagsinput"));
    }
}
