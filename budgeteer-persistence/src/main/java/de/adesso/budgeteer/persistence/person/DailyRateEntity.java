package de.adesso.budgeteer.persistence.person;

import de.adesso.budgeteer.persistence.budget.BudgetEntity;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import org.joda.money.Money;

@Entity
@Table(name = "DAILY_RATE")
@Data
public class DailyRateEntity {

  @Id @GeneratedValue private long id;

  @Temporal(TemporalType.DATE)
  private Date dateEnd;

  @Temporal(TemporalType.DATE)
  private Date dateStart;

  @ManyToOne(optional = false)
  @JoinColumn(name = "PERSON_ID")
  private PersonEntity person;

  @ManyToOne(optional = false)
  @JoinColumn(name = "BUDGET_ID")
  private BudgetEntity budget;

  private Money rate;
}
