package org.wickedsource.budgeteer.web.pages.administration;

import de.adesso.budgeteer.common.date.DateRange;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectModel implements Serializable {
  private long projectId;
  private String name;
  private DateRange dateRange;
}
