package org.wickedsource.budgeteer.web.pages.templates.templateimport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.wickedsource.budgeteer.service.ReportType;

@Data
public class TemplateFormInputDto implements Serializable {
  private long projectId;
  private String name;
  private String description;
  private ReportType type;
  private transient List<FileUpload> fileUploads;
  private boolean isDefault;

  public TemplateFormInputDto(long projectId) {
    this.projectId = projectId;
  }

  public TemplateFormInputDto(
      long projectId, String name, String description, ReportType type, boolean isDefault) {
    this.projectId = projectId;
    this.name = name;
    this.description = description;
    this.type = type;
    this.fileUploads = new ArrayList<>();
    this.isDefault = isDefault;
  }
}
