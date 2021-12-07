package de.adesso.budgeteer.core.contract.domain;

import lombok.Value;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Value
public class ContractSummary {
    Contract contract;
    LocalDate from;
    LocalDate until;
    Money budgetSpent;
    Money budgetLeft;
    Money invoicedBudget;

    public BigDecimal ratioBurned() {
        return budgetSpent.getAmount().divide(contract.getBudget().getAmount(), RoundingMode.HALF_DOWN);
    }

    public Money totalBudget() {
        return contract.getBudget();
    }

    public Money totalBudgetGross() {
        return multiplyWithTaxRate(contract.getBudget());
    }

    public Money budgetLeftGross() {
        return multiplyWithTaxRate(budgetLeft);
    }

    public Money budgetSpentGross() {
        return multiplyWithTaxRate(budgetSpent);
    }

    private Money multiplyWithTaxRate(Money money) {
        return money.multipliedBy(contract.taxRateAsCoefficient(), RoundingMode.HALF_DOWN);
    }
}
