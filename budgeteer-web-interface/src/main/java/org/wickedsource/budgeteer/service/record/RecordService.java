package org.wickedsource.budgeteer.service.record;

import de.adesso.budgeteer.persistence.record.WorkRecordEntity;
import de.adesso.budgeteer.persistence.record.WorkRecordRepository;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.ListUtil;

@Service
@Transactional
public class RecordService {

  @Autowired private WorkRecordRepository workRecordRepository;

  @Autowired private WorkRecordMapper recordMapper;

  /**
   * Loads the records from the database that match the given filter. If a filter criterion is left
   * empty (null) it will not be applied.
   *
   * @param filter the filter to apply when loading records.
   * @return filtered list of records.
   */
  public List<WorkRecord> getFilteredRecords(WorkRecordFilter filter) {
    Specification<WorkRecordEntity> query = WorkRecordQueries.findByFilter(filter);
    List<WorkRecord> workRecords =
        recordMapper.map(ListUtil.toArrayList(workRecordRepository.findAll(query)));
    switch (filter.getColumnToSort().getObject()) {
      case BUDGET:
        workRecords.sort(Comparator.comparing(WorkRecord::getBudgetName));
        break;
      case NAME:
        workRecords.sort(Comparator.comparing(WorkRecord::getPersonName));
        break;
      case DAILY_RATE:
        workRecords.sort(Comparator.comparing(WorkRecord::getDailyRate));
        break;
      case DATE:
        workRecords.sort(Comparator.comparing(WorkRecord::getDate));
        break;
      case HOURS:
        workRecords.sort(Comparator.comparing(WorkRecord::getHours));
        break;
      case BUDGET_BURNED:
        workRecords.sort(Comparator.comparing(WorkRecord::getBudgetBurned));
        break;
    }
    if (filter.getSortType().getObject().equals("Descending")) {
      Collections.reverse(workRecords);
    }
    return workRecords;
  }

  public void saveDailyRateForWorkRecord(WorkRecord record) {
    WorkRecordEntity entity =
        workRecordRepository.findById(record.getId()).orElseThrow(RuntimeException::new);
    entity.setDailyRate(record.getDailyRate());
    entity.setEditedManually(record.isEditedManually());
    workRecordRepository.save(entity);
  }
}
