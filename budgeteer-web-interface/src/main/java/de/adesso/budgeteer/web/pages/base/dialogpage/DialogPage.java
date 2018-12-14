package de.adesso.budgeteer.web.pages.base.dialogpage;

import de.adesso.budgeteer.web.BudgeteerReferences;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class DialogPage extends WebPage {

    public DialogPage(PageParameters parameters) {
        super(parameters);
        add(new HeaderResponseContainer("JavaScriptContainer", "JavaScriptContainer"));
    }

    public DialogPage() {
        this(null);
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getJQueryReference()));
        response.render(JavaScriptReferenceHeaderItem.forReference(BudgeteerReferences.getAdminLteAppReference()));
        response.render(JavaScriptReferenceHeaderItem.forUrl("//maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"));

    }




}
