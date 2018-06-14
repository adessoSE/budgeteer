package org.wickedsource.budgeteer.persistence.record;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "WORK_RECORD", indexes = {
        @Index(name = "WORK_RECORD_BUDGET_ID_IDX", columnList = "BUDGET_ID"),
        @Index(name = "WORK_RECORD_PERSON_ID_IDX", columnList = "PERSON_ID")
})
public class WorkRecordEntity extends RecordEntity {

    @Column(name="EDITED_MANUALLY")
    private Boolean editedManually;

    public Boolean isEditedManually() {
        return editedManually;
    }

    public void setEditedManually(Boolean editedManually) {
        this.editedManually = editedManually;
    }
}
