package de.adesso.budgeteer.persistence.person;

import de.adesso.budgeteer.persistence.project.ProjectEntity;
import de.adesso.budgeteer.persistence.record.PlanRecordEntity;
import de.adesso.budgeteer.persistence.record.WorkRecordEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.joda.money.Money;

@Entity
@Data
@Table(
    name = "PERSON",
    indexes = {@Index(name = "PERSON_PROJECT_ID_IDX", columnList = "PROJECT_ID")})
public class PersonEntity {

  @Id @GeneratedValue private long id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(nullable = false, length = 255)
  private String importKey;

  @Column private Money defaultDailyRate;

  @ManyToOne(optional = false)
  private ProjectEntity project;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "person",
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private List<DailyRateEntity> dailyRates = new ArrayList<>();

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "person",
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private List<WorkRecordEntity> workRecords = new ArrayList<>();

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "person",
      orphanRemoval = true,
      cascade = CascadeType.ALL)
  private List<PlanRecordEntity> planRecords = new ArrayList<>();
}
