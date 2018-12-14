package de.adesso.budgeteer.service.budget;

import de.adesso.budgeteer.persistence.budget.BudgetEntity;
import org.springframework.stereotype.Component;
import de.adesso.budgeteer.service.AbstractMapper;

@Component
public class BudgetBaseDataMapper extends AbstractMapper<BudgetEntity, BudgetBaseData> {

    @Override
    public BudgetBaseData map(BudgetEntity entity) {
        BudgetBaseData data = new BudgetBaseData();
        data.setId(entity.getId());
        data.setName(entity.getName());
        return data;
    }
}
