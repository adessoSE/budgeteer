package de.adesso.budgeteer.persistence.person;

import de.adesso.budgeteer.common.date.DateRange;
import de.adesso.budgeteer.core.person.domain.PersonRate;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonRateMapper {
  public PersonRate mapToDomain(DailyRateEntity dailyRateEntity) {
    return new PersonRate(
        dailyRateEntity.getRate(),
        dailyRateEntity.getBudget().getId(),
        new DateRange(
            new Date(dailyRateEntity.getDateStart().getTime()).toLocalDate(),
            new Date(dailyRateEntity.getDateEnd().getTime()).toLocalDate()));
  }

  public List<PersonRate> mapToDomain(List<DailyRateEntity> dailyRateEntities) {
    return dailyRateEntities.stream().map(this::mapToDomain).collect(Collectors.toList());
  }
}
