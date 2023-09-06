package org.wickedsource.budgeteer.web.components.fileUpload;

import static org.apache.wicket.model.LambdaModel.of;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class CustomFileUpload extends GenericPanel<FileUploadModel> {
  private final FileUploadField uploadField;

  public CustomFileUpload(String id, IModel<FileUploadModel> model) {
    super(id, model);

    add(
        new TextField<String>(
            "link", of(getModel(), FileUploadModel::getLink, FileUploadModel::setLink)));

    final Label fileName = new Label("fileName", getModel().map(FileUploadModel::getFileName));
    add(fileName);
    uploadField = new FileUploadField("fileUpload");
    uploadField.add(
        new AjaxEventBehavior("change") {
          @Override
          protected void onEvent(AjaxRequestTarget target) {
            getModelObject().setChanged(true);
          }
        });
    add(uploadField);

    WebMarkupContainer deleteButton = new WebMarkupContainer("delete");
    deleteButton.add(
        new AjaxEventBehavior("click") {
          @Override
          protected void onEvent(AjaxRequestTarget target) {
            getModelObject().setChanged(false);
            getModelObject().setFileName(null);
            getModelObject().setFile(null);
            target.add(uploadField);
            target.add(fileName);
          }
        });
    add(deleteButton);
  }

  public byte[] getFile() {
    byte[] result;
    if (getModelObject().isChanged()) {
      result = uploadField.getFileUpload().getBytes();
    } else {
      result = getModelObject().getFile();
    }
    return result;
  }

  public String getFileName() {
    String result;
    if (getModelObject().isChanged()) {
      result = uploadField.getFileUpload().getClientFileName();
    } else {
      result = getModelObject().getFileName();
    }
    return result;
  }
}
