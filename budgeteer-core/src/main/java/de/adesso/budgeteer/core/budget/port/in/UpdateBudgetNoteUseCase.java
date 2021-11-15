package de.adesso.budgeteer.core.budget.port.in;

public interface UpdateBudgetNoteUseCase {
    void updateBudgetNote(long budgetId, String note);
}
