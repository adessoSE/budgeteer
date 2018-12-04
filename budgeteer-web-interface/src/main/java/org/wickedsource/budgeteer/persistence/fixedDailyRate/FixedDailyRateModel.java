package org.wickedsource.budgeteer.persistence.fixedDailyRate;

import lombok.Getter;
import lombok.Setter;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRate;
import org.wickedsource.budgeteer.service.fixedDailyRate.FixedDailyRateService;

import java.util.List;

public class FixedDailyRateModel extends LoadableDetachableModel<List<FixedDailyRate>> {
    @SpringBean
    private FixedDailyRateService service;

    @Getter
    @Setter
    private long budgetId;

    public FixedDailyRateModel(long budgetId, FixedDailyRateService service) {
        this.budgetId = budgetId;
        this.service = service;
    }

    @Override
    protected List<FixedDailyRate> load() {
        return service.getFixedDailyRates(budgetId);
    }
}
