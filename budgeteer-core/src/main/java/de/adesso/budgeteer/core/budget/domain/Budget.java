package de.adesso.budgeteer.core.budget.domain;

import java.util.Date;
import java.util.List;
import lombok.Value;
import org.joda.money.Money;

@Value
public class Budget {
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
  Money unplanned;
  Money limit;
  Date lastUpdated;
  List<String> tags;
}
