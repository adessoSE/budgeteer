package org.wickedsource.budgeteer.persistence.record;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "PLAN_RECORD", indexes = {@Index(name = "PLAN_RECORD_BUDGET_ID_IDX", columnList = "BUDGET_ID")})
public class PlanRecordEntity extends RecordEntity {

}
