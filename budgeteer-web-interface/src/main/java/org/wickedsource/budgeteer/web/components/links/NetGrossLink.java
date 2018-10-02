package org.wickedsource.budgeteer.web.components.links;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.tax.TaxSwitchLabelModel;

public class NetGrossLink extends Panel {

    public NetGrossLink(String id)
    {
        super(id);
        Link link = new Link("link") {
            @Override
            public void onClick() {
                if (BudgeteerSession.get().isTaxEnabled() && Math.abs(BudgeteerSession.get().getSelectedBudgetUnit() - 1.0) < Math.ulp(1)) {
                    BudgeteerSession.get().setTaxEnabled(false);
                } else {
                    BudgeteerSession.get().setTaxEnabled(true);
                }
            }
        };

        if(Math.abs(BudgeteerSession.get().getSelectedBudgetUnit() - 1.0) > Math.ulp(1)){
            link.add(new AttributeAppender("style", "cursor: not-allowed;", " "));
            link.add(new AttributeModifier("title", new StringResourceModel("links.budge.label.gross.value", this)));
            link.setEnabled(false);
            BudgeteerSession.get().setTaxEnabled(false);
        }
        link.add(new Label("title", new TaxSwitchLabelModel<>(
                new StringResourceModel("links.tax.label.net", this),
                new StringResourceModel("links.tax.label.gross", this)
        )));
        this.add(link);

    }
}
