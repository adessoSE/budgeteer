package org.wickedsource.budgeteer.persistence.record;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MissingDailyRateBean {

    private long personId;

    private String personName;

    private Date startDate;

    private Date endDate;
}
