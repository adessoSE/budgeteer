package org.wickedsource.budgeteer.web.pages.templates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.wickedsource.budgeteer.service.ReportType;

@Getter
public class TemplateFilter implements Serializable {

  private final long projectId;
  private final List<ReportType> typesList;
  private final List<ReportType> possibleTypes;

  public TemplateFilter(long projectId) {
    this.projectId = projectId;
    this.typesList = new ArrayList<>(Arrays.asList(ReportType.values()));
    this.possibleTypes = new ArrayList<>();
  }
}
