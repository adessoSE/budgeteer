package de.adesso.budgeteer.rest.budget.model;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.money.Money;

@Data
@AllArgsConstructor
public class BudgetModel {
  long id;
  Long contractId;
  String name;
  String contractName;
  String description;
  String importKey;
  Money total;
  Money spent;
  Money remaining;
  Money averageDailyRate;
  Money limit;
  Date lastUpdated;
  List<String> tags;
}
