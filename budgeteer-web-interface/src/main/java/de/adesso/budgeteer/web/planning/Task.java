package de.adesso.budgeteer.web.planning;

import de.adesso.budgeteer.MoneyUtil;
import org.joda.money.Money;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private final String name;

    private final Money totalBudget;

    private Money restBudget;

    private Money allocatedBudget;

    private final List<Allocation> allocations = new ArrayList<>();

    private boolean isOverspent = false;

    public Task(String name, Money totalBudget) {
        this.name = name;
        this.totalBudget = totalBudget;
    }

    public String getName() {
        return name;
    }

    public Money getTotalBudget() {
        return totalBudget;
    }

    public List<Allocation> getAllocations() {
        return allocations;
    }

    public boolean isOverspent() {
        return isOverspent;
    }

    protected void setOverspent(boolean isOverspent) {
        this.isOverspent = isOverspent;
    }

    public synchronized void recalculate(Configuration config) {
        allocatedBudget = MoneyUtil.ZERO;
        restBudget = MoneyUtil.ZERO;

        for (Allocation allocation : allocations) {
            int workingDays = config.getCalendar().getNumberOfWorkingDays(allocation.getPerson().getAbsences());
            Money spentBudget = MoneyUtil.ZERO;
            spentBudget = spentBudget.plus(allocation.getPerson().getDailyRate().multipliedBy(workingDays));
            spentBudget = allocation.getWorkload().of(spentBudget);
            allocatedBudget = allocatedBudget.plus(spentBudget);
        }
        restBudget = totalBudget.minus(allocatedBudget);
        if(restBudget.isNegative()){
            isOverspent = true;
        }else{
            isOverspent = false;
        }
    }

    public Money getRestBudget() {
        return restBudget;
    }

    public Money getAllocatedBudget() {
        return allocatedBudget;
    }
}
