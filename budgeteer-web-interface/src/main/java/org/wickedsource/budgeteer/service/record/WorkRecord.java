package org.wickedsource.budgeteer.service.record;

import lombok.Data;
import org.joda.money.Money;

import java.io.Serializable;
import java.util.Date;

@Data
public class WorkRecord implements Serializable {
    private String budgetName;
    private String personName;
    private Date date;
    private double hours;
    private Money budgetBurned;
    private Money dailyRate;

}

