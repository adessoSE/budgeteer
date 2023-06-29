package de.adesso.budgeteer.persistence.record;

import java.util.Date;
import org.joda.money.Money;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository {

  void deleteByImport(long importId);

  void deleteByImportAndProjectId(long projectId);

  void updateDailyRates(long budgetId, long personId, Date fromDate, Date toDate, Money dailyRate);

  Long countByProjectId(long projectId);
}
