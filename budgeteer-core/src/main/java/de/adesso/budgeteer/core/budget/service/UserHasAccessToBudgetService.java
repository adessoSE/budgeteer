package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.port.in.UserHasAccessToBudgetUseCase;
import de.adesso.budgeteer.core.budget.port.out.UserHasAccessToBudgetPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHasAccessToBudgetService implements UserHasAccessToBudgetUseCase {
    private final UserHasAccessToBudgetPort userHasAccessToBudgetPort;

    @Override
    public boolean userHasAccessToBudget(String username, long budgetId) {
        return userHasAccessToBudgetPort.userHasAccessToBudget(username, budgetId);
    }
}
