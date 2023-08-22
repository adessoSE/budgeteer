package org.wickedsource.budgeteer.web.pages.templates.edit;

import static org.apache.wicket.model.LambdaModel.of;

import java.util.Arrays;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.wickedsource.budgeteer.service.ReportType;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.TemplateFormInputDto;

public class EditTemplateInputPanel extends GenericPanel<TemplateFormInputDto> {

  private static final AttributeModifier starChecked =
      new AttributeModifier("class", "btn bg-olive glyphicon glyphicon-star");
  private static final AttributeModifier starUnchecked =
      new AttributeModifier("class", "btn bg-olive glyphicon glyphicon-star-empty");

  public EditTemplateInputPanel(String id, IModel<TemplateFormInputDto> model) {
    super(id, model);

    var form = new Form<>("form", model);

    form.add(
        new FileUploadField(
                "fileUpload",
                LambdaModel.of(
                    model,
                    TemplateFormInputDto::getFileUploads,
                    TemplateFormInputDto::setFileUploads))
            .setRequired(false));

    var checkBox =
        new AjaxLink<>("setAsDefault") {
          @Override
          public void onClick(AjaxRequestTarget ajaxRequestTarget) {
            model.getObject().setDefault(!model.getObject().isDefault());
            add(model.getObject().isDefault() ? starUnchecked : starChecked);
            ajaxRequestTarget.add(this, this.getMarkupId());
          }
        }.add(model.getObject().isDefault() ? starUnchecked : starChecked).setOutputMarkupId(true);
    form.add(checkBox);

    form.add(
        new RequiredTextField<>(
            "name", of(model, TemplateFormInputDto::getName, TemplateFormInputDto::setName)));
    form.add(
        new TextField<>(
            "description",
            of(model, TemplateFormInputDto::getDescription, TemplateFormInputDto::setDescription)));
    var typeDropDown =
        new DropDownChoice<>(
                "type",
                of(model, TemplateFormInputDto::getType, TemplateFormInputDto::setType),
                Arrays.asList(ReportType.values()),
                new AbstractChoiceRenderer<>() {
                  @Override
                  public Object getDisplayValue(ReportType object) {
                    return object == null ? "Unnamed" : object.toString();
                  }
                })
            .setNullValid(false)
            .setRequired(true);
    form.add(typeDropDown);
    add(form);
  }
}
