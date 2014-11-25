package org.wickedsource.budgeteer.web.pages.budgets.overview.table.progressbar;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;

public class ProgressBar extends Panel {

    public ProgressBar(String id, IModel<Double> percentModel) {
        super(id, percentModel);
        add(createBar("bar"));
    }

    private Label createBar(String id) {
        Label bar = new Label(id, new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                return String.format(WebSession.get().getLocale(),"%.2f%%", getModel().getObject());
            }
        });
        bar.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                Double progress = getModel().getObject();
                if (progress > 90d) {
                    return " progress-bar-danger";
                } else if (progress > 75d) {
                    return " progress-bar-warning";
                } else {
                    return " progress-bar-success";
                }
            }
        }));
        bar.add(new AttributeAppender("style", new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                int value = getModel().getObject().intValue();
                if(value < 0 ){
                    value = 0;
                }
                if(value > 100){
                    value = 100;
                }
                return String.format("width: %d%%", value);
            }
        }));
        return bar;
    }

    @SuppressWarnings("unchecked")
    private IModel<Double> getModel() {
        return (IModel<Double>) getDefaultModel();
    }
}
