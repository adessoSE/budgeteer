package de.adesso.budgeteer.core.budget.port.out;

public interface UpdateBudgetNotePort {
    void updateBudgetNote(long budgetId, String note);
}
