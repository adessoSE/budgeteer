package org.wickedsource.budgeteer.web.pages.budgets.overview.report.form;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.service.report.BudgetReportMetaInformation;

public class BudgetReportModel implements IModel<BudgetReportMetaInformation>, IObjectClassAwareModel<BudgetReportMetaInformation> {

	private IModel<BudgetReportMetaInformation> wrappedModel;

	public BudgetReportModel(BudgetReportMetaInformation metaInfo) {
		this.wrappedModel = Model.of(metaInfo);
	}
	
	public BudgetReportModel(IModel<BudgetReportMetaInformation> wrappedModel) {
		this.wrappedModel = wrappedModel;
	}
	
	@Override
	public void detach() {
		wrappedModel.detach();
	}

	@Override
	public Class<BudgetReportMetaInformation> getObjectClass() {
		return BudgetReportMetaInformation.class;
	}

	@Override
	public BudgetReportMetaInformation getObject() {
		return wrappedModel.getObject();
	}

	@Override
	public void setObject(BudgetReportMetaInformation object) {
		wrappedModel.setObject(object);
	}


}
