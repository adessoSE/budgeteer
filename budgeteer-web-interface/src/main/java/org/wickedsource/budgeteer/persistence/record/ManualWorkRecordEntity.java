package org.wickedsource.budgeteer.persistence.record;

import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "WORK_RECORD_MANUALLY", indexes = {
        @Index(name = "WORK_RECORD_MANUALLY_BUDGET_ID_IDX", columnList = "BUDGET_ID"),
        @Index(name = "WORK_RECORD_MANUALLY_PERSON_ID_IDX", columnList = "PERSON_ID")
})
@Getter
@Setter
public class ManualWorkRecordEntity extends WorkRecordEntity {
    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private Money moneyAmount;
}
