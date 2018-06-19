package org.wickedsource.budgeteer.service.record;

import lombok.Data;
import org.apache.poi.hpsf.SummaryInformation;
import org.joda.money.Money;
import org.wickedsource.budgeteer.MoneyUtil;

import javax.xml.transform.Result;
import java.util.*;

@Data
public class AggregatedRecord {
    private String aggregationPeriodTitle;
    private Date aggregationPeriodStart;
    private Date aggregationPeriodEnd;
    private Double hours;
    private Money budgetPlanned;
    private Money budgetBurned;
    private Money budgetPlanned_gross;
    private Money budgetBurned_gross;

    public Money getDifference() {
        Money first = budgetPlanned != null ? budgetPlanned : MoneyUtil.createMoney(0);
        Money second = budgetBurned != null ? budgetBurned : MoneyUtil.createMoney(0);
        return first.minus(second);
    }

    public Money getDifference_gross(){
        Money first = budgetPlanned_gross != null ? budgetPlanned_gross : MoneyUtil.createMoney(0);
        Money second = budgetBurned_gross != null ? budgetBurned_gross : MoneyUtil.createMoney(0);
        return first.minus(second);
    }
}
