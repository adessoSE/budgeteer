package de.adesso.budgeteer.core.contract.domain;

import de.adesso.budgeteer.core.common.Attachment;
import lombok.Value;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Value
public class Contract {
    long id;
    long projectId;
    String internalNumber;
    String name;
    Type type;
    LocalDate startDate;
    Money budget;
    Money budgetSpent;
    Money budgetLeft;
    BigDecimal taxRate;
    Map<String, String> attributes;
    Attachment attachment;

    public BigDecimal taxRateAsCoefficient() {
        return BigDecimal.ONE.add(taxRate.divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN));
    }

    public enum Type {
        TIME_AND_MATERIAL,
        FIXED_PRICE
    }
}
