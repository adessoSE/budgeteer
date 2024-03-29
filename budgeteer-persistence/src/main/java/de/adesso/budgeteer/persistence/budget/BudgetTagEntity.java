package de.adesso.budgeteer.persistence.budget;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "BUDGET_TAG",
    indexes = {@Index(name = "BUDGET_TAG_BUDGET_ID_IDX", columnList = "BUDGET_ID")})
@Data
@NoArgsConstructor
public class BudgetTagEntity {

  @Id @GeneratedValue private long id;

  @ManyToOne
  @JoinColumn(name = "BUDGET_ID")
  private BudgetEntity budget;

  private String tag;

  public BudgetTagEntity(BudgetEntity budget, String tag) {
    this.budget = budget;
    this.tag = tag;
  }

  public BudgetTagEntity(String tag) {
    this.tag = tag;
  }
}
