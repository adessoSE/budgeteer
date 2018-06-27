package org.wickedsource.budgeteer.web.pages.budgets.overview.report.form;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.Model;
import org.wickedsource.budgeteer.service.budget.report.ReportMetaInformation;

public class BudgetReportModel implements IModel<ReportMetaInformation>, IObjectClassAwareModel<ReportMetaInformation> {

	private IModel<ReportMetaInformation> wrappedModel;

	public BudgetReportModel(ReportMetaInformation metaInfo) {
		this.wrappedModel = Model.of(metaInfo);
	}

	public BudgetReportModel(IModel<ReportMetaInformation> wrappedModel) {
		this.wrappedModel = wrappedModel;
	}

	@Override
	public void detach() {
		wrappedModel.detach();
	}

	@Override
	public Class<ReportMetaInformation> getObjectClass() {
		return ReportMetaInformation.class;
	}

	@Override
	public ReportMetaInformation getObject() {
		return wrappedModel.getObject();
	}

	@Override
	public void setObject(ReportMetaInformation object) {
		wrappedModel.setObject(object);
	}


}
