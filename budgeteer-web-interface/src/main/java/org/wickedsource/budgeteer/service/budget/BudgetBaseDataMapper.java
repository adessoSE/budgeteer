package org.wickedsource.budgeteer.service.budget;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.service.AbstractMapper;

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
