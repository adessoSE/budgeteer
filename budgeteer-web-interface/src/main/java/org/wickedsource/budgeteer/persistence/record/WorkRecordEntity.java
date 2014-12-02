package org.wickedsource.budgeteer.persistence.record;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "WORK_RECORD", indexes = {
        @Index(name = "WORK_RECORD_BUDGET_ID_IDX", columnList = "BUDGET_ID"),
        @Index(name = "WORK_RECORD_PERSON_ID_IDX", columnList = "PERSON_ID")
})
public class WorkRecordEntity extends RecordEntity {

}
