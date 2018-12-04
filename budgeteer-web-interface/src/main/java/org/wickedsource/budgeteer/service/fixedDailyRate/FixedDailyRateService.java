package org.wickedsource.budgeteer.service.fixedDailyRate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateEntity;
import org.wickedsource.budgeteer.persistence.fixedDailyRate.FixedDailyRateRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FixedDailyRateService {
    @Autowired
    private FixedDailyRateRepository fixedDailyRateRepository;
    @Autowired
    private BudgetRepository budgetRepository;

    public List<FixedDailyRate> getFixedDailyRates(long budgetId) {
        List<FixedDailyRateEntity> entities = fixedDailyRateRepository.getFixedDailyRateByBudgetId(budgetId);
        List<FixedDailyRate> result = new ArrayList<>();
        for (FixedDailyRateEntity entity : entities) {
            FixedDailyRate data = new FixedDailyRate(entity);
            result.add(data);
        }
        return result;
    }

    public FixedDailyRate loadFixedDailyRate(long fixedDailyRateId) {
        return new FixedDailyRate(fixedDailyRateRepository.findOne(fixedDailyRateId));
    }

    public long saveFixedDailyRate(FixedDailyRate data) {
        assert data != null;
        FixedDailyRateEntity entity = new FixedDailyRateEntity();
        entity.setId(data.getId());
        entity.setMoneyAmount(data.getMoneyAmount());
        entity.setDescription(data.getDescription());
        entity.setName(data.getName());
        entity.setStartDate(data.getDateRange().getStartDate());
        entity.setEndDate(data.getDateRange().getEndDate());
        BudgetEntity budgetEntity = budgetRepository.findOne(data.getBudgetId());
        entity.setBudget(budgetEntity);
        fixedDailyRateRepository.save(entity);
        return entity.getId();
    }

    public void deleteFixedDailyRate(long id) {
        fixedDailyRateRepository.delete(id);
    }
}
