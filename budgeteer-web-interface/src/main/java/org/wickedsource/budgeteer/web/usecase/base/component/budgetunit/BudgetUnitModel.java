package org.wickedsource.budgeteer.web.usecase.base.component.budgetunit;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.settings.BudgetUnit;
import org.wickedsource.budgeteer.service.settings.SettingsService;

import java.util.List;

public class BudgetUnitModel extends LoadableDetachableModel<List<BudgetUnit>> {

    @SpringBean
    private SettingsService service;

    private long userId;

    public BudgetUnitModel(long userId) {
        Injector.get().inject(this);
        this.userId = userId;
    }

    @Override
    protected List<BudgetUnit> load() {
        return service.getBudgetUnits(userId);
    }
}
