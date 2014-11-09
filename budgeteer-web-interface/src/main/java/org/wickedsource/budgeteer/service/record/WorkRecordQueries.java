package org.wickedsource.budgeteer.service.record;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.wickedsource.budgeteer.persistence.record.QWorkRecordEntity;

public class WorkRecordQueries {

    public static Predicate findByFilter(WorkRecordFilter filter) {
        QWorkRecordEntity record = QWorkRecordEntity.workRecordEntity;
        BooleanExpression expression = record.budget.project.id.eq(filter.getProjectId());
        if (filter.getDateRange() != null && filter.getDateRange().getStartDate() != null) {
            expression = expression.and(record.date.goe(filter.getDateRange().getStartDate()));
        }
        if (filter.getDateRange() != null && filter.getDateRange().getEndDate() != null) {
            expression = expression.and(record.date.loe(filter.getDateRange().getEndDate()));
        }
        if (filter.getBudget() != null && filter.getBudget().getId() >= 0l) {
            expression = expression.and(record.budget.id.eq(filter.getBudget().getId()));
        }
        if (filter.getPerson() != null && filter.getPerson().getId() >= 0l) {
            expression = expression.and(record.person.id.eq(filter.getPerson().getId()));
        }
        return expression;
    }

}
