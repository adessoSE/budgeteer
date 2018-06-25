package org.wickedsource.budgeteer.service.record;

import java.math.RoundingMode;

import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.service.AbstractMapper;

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
