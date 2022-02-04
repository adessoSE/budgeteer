package de.adesso.budgeteer.persistence.record;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissingDailyRateBean implements Serializable {

  private long personId;

  private String personName;

  private Date startDate;

  private Date endDate;
}
