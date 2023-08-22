package org.wickedsource.budgeteer.web.pages.contract.edit.form;

import static org.apache.wicket.model.LambdaModel.of;

import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;

public class EditContractAdditionalAttributesPanel
    extends GenericPanel<List<DynamicAttributeField>> {
  public EditContractAdditionalAttributesPanel(
      String id, IModel<List<DynamicAttributeField>> model, Component feedbackPanel) {
    super(id, model);
    var form = new Form<>("form", model);

    var table = new WebMarkupContainer("attributeTable");
    table.setOutputMarkupId(true);
    table.setOutputMarkupPlaceholderTag(true);
    table.add(
        new ListView<>("contractAttributes", model) {
          @Override
          protected void populateItem(ListItem<DynamicAttributeField> item) {
            item.add(
                new Label("attributeTitle", item.getModel().map(DynamicAttributeField::getName)));
            item.add(
                new TextField<>(
                    "attributeValue",
                    of(
                        item.getModel(),
                        DynamicAttributeField::getValue,
                        DynamicAttributeField::setValue)));
          }
        });
    form.add(table);

    var newAttributeField = new TextField<>("nameOfNewAttribute", Model.of(""));
    newAttributeField.setOutputMarkupId(true);
    form.add(newAttributeField);
    var addAttribute =
        new AjaxButton("addAttribute") {
          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            if (newAttributeField.getModelObject() != null) {
              model
                  .getObject()
                  .add(new DynamicAttributeField(newAttributeField.getModelObject(), ""));
              target.add(table, newAttributeField, feedbackPanel);
            } else {
              this.error(getString("feedback.error.nameEmpty"));
              target.add(feedbackPanel);
            }
          }
        };

    addAttribute.setOutputMarkupId(true);
    form.add(addAttribute);
    add(form);
  }
}
