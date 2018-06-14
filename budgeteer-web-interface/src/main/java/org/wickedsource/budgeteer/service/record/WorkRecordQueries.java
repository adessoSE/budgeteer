package org.wickedsource.budgeteer.service.record;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.wickedsource.budgeteer.persistence.record.QWorkRecordEntity;

public class WorkRecordQueries {

    /**
     * @deprecated should be re-implemented using criteria api to load "flat" objects instead of a WorkRecordEntity!!!
     */
    @Deprecated
    public static Predicate findByFilter(WorkRecordFilter filter) {
        QWorkRecordEntity record = QWorkRecordEntity.workRecordEntity;
        BooleanExpression expression = record.budget.project.id.eq(filter.getProjectId());
        if (filter.getDateRange() != null && filter.getDateRange().getStartDate() != null) {
            expression = expression.and(record.date.goe(filter.getDateRange().getStartDate()));
        }
        if (filter.getDateRange() != null && filter.getDateRange().getEndDate() != null) {
            expression = expression.and(record.date.loe(filter.getDateRange().getEndDate()));
        }
        BooleanExpression budgetExpression = getBudgetExpression(filter, record);
        if(budgetExpression != null) {
            expression = expression.and(budgetExpression);
        }
        BooleanExpression personExpression = getPersonExpression(filter, record);
        if(personExpression != null) {
            expression = expression.and(personExpression);
        }
        return expression;
    }

    private static BooleanExpression getBudgetExpression(WorkRecordFilter filter, QWorkRecordEntity record) {
        BooleanExpression result = null;
        for (int index = 0; index < filter.getBudgetList().size(); index++) {
            if(index == 0){
                result = record.budget.id.eq(filter.getBudgetList().get(index).getId());
            } else {
                result = result.or(record.budget.id.eq(filter.getBudgetList().get(index).getId()));
            }

        }
        return result;
    }

    private static BooleanExpression getPersonExpression(WorkRecordFilter filter, QWorkRecordEntity record) {
        BooleanExpression result = null;
        for (int index = 0; index < filter.getPersonList().size(); index++) {
            if(index == 0){
                result = record.person.id.eq(filter.getPersonList().get(index).getId());
            } else {
                result = result.or(record.person.id.eq(filter.getPersonList().get(index).getId()));
            }

        }
        return result;
    }

}
