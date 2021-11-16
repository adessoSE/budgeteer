package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.port.in.GetBudgetNoteUseCase;
import de.adesso.budgeteer.core.budget.port.out.GetBudgetNotePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBudgetNoteService implements GetBudgetNoteUseCase {

    private final GetBudgetNotePort getBudgetNotePort;

    @Override
    public String getBudgetNote(long budgetId) {
        return getBudgetNotePort.getBudgetNote(budgetId);
    }
}
