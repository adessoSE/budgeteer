package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.port.in.UpdateBudgetNoteUseCase;
import de.adesso.budgeteer.core.budget.port.out.UpdateBudgetNotePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBudgetNoteService implements UpdateBudgetNoteUseCase {

    private final UpdateBudgetNotePort updateBudgetNotePort;

    @Override
    public void updateBudgetNote(long budgetId, String note) {
        updateBudgetNotePort.updateBudgetNote(budgetId, note);
    }
}
