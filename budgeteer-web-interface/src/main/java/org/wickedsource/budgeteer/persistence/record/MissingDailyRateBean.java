package org.wickedsource.budgeteer.persistence.record;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class MissingDailyRateBean implements Serializable {

    private long personId;

    private String personName;

    private Date startDate;

    private Date endDate;
}
