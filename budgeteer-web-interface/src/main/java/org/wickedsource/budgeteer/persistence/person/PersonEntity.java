package org.wickedsource.budgeteer.persistence.person;

import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PERSON")
public class PersonEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String importKey;

    @ManyToOne(optional = false)
    private ProjectEntity project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<DailyRateEntity> dailyRates = new ArrayList<DailyRateEntity>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<WorkRecordEntity> workRecords = new ArrayList<WorkRecordEntity>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImportKey() {
        return importKey;
    }

    public void setImportKey(String importKey) {
        this.importKey = importKey;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public List<DailyRateEntity> getDailyRates() {
        return dailyRates;
    }

    public void setDailyRates(List<DailyRateEntity> dailyRates) {
        this.dailyRates = dailyRates;
    }

    public List<WorkRecordEntity> getWorkRecords() {
        return workRecords;
    }

    public void setWorkRecords(List<WorkRecordEntity> workRecords) {
        this.workRecords = workRecords;
    }
}
