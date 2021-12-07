package de.adesso.budgeteer.common.date;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Value
public class DateRange implements Serializable {
    LocalDate startDate;
    LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate may not be null. " +
                    "If you want to specify an indefinite timeframe use the default constructor");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate must be after startDate");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public DateRange() {
        this.startDate = null;
        this.endDate = null;
    }

    public boolean isIndefinite() {
        return startDate == null;
    }
}
