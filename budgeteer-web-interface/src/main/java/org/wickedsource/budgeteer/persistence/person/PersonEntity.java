package org.wickedsource.budgeteer.persistence.person;

import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.record.PlanRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "PERSON", indexes = {@Index(name = "PERSON_PROJECT_ID_IDX", columnList = "PROJECT_ID")})
public class PersonEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String importKey;

    @Column
    private Money defaultDailyRate;

    @ManyToOne(optional = false)
    private ProjectEntity project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<DailyRateEntity> dailyRates = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<WorkRecordEntity> workRecords = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PlanRecordEntity> planRecords = new ArrayList<>();
}
