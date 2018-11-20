package org.wickedsource.budgeteer.web.components;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class MarqueeLabel extends Panel {

    public MarqueeLabel(String id, IModel<String> text){
        super(id);
        Label label = new Label("label", text);
        label.setOutputMarkupId(true);
        WebMarkupContainer markupContainer = new WebMarkupContainer("div"){
            @Override
            public void renderHead(IHeaderResponse response) {
                super.renderHead(response);
                this.setOutputMarkupId(true);
                response.render(JavaScriptHeaderItem.forScript(
                        "       var label = document.getElementById('"+ label.getMarkupId() +"');\n" +
                        "                var div = document.getElementById('"+ this.getMarkupId() + "');\n" +
                        "\n" +
                        "                if (label.offsetWidth < label.scrollWidth) {\n" +
                        "                    div.classList.add(\"marquee\");\n" +
                        "                }", "id" + label.getMarkupId()));
            }
        };
        markupContainer.add(label);
        this.add(markupContainer);
    }
}
