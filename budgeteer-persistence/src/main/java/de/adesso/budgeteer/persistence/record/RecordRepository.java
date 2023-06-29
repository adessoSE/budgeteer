package de.adesso.budgeteer.persistence.record;

import org.joda.money.Money;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RecordRepository {

  void deleteByImport(long importId);

  void deleteByImportAndProjectId(long projectId);

  void updateDailyRates(long budgetId, long personId, Date fromDate, Date toDate, Money dailyRate);

  Long countByProjectId(long projectId);
}
