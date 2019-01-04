package de.adesso.budgeteer.service.record;

import de.adesso.budgeteer.persistence.record.WorkRecordEntity;
import de.adesso.budgeteer.service.AbstractMapper;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Component
public class WorkRecordMapper extends AbstractMapper<WorkRecordEntity, WorkRecord> {

    @Override
    public WorkRecord map(WorkRecordEntity entity) {
        WorkRecord record = new WorkRecord();
        record.setId(entity.getId());
        record.setBudgetBurned(entity.getDailyRate().multipliedBy(entity.getMinutes()).dividedBy(60, RoundingMode.FLOOR).dividedBy(8, RoundingMode.FLOOR));
        record.setPersonName(entity.getPerson().getName());
        record.setHours(entity.getMinutes() / 60d);
        record.setDate(entity.getDate());
        record.setBudgetName(entity.getBudget().getName());
        record.setDailyRate(entity.getDailyRate());
        record.setEditedManually(entity.isEditedManually() != null ? entity.isEditedManually(): false);
        return record;
    }
}
