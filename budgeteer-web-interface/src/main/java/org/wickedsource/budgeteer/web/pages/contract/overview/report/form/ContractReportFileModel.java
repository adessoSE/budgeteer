package org.wickedsource.budgeteer.web.pages.contract.overview.report.form;

import java.io.File;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.contract.report.ContractReportService;
import org.wickedsource.budgeteer.web.pages.contract.overview.report.ContractReportMetaInformation;

public class ContractReportFileModel extends LoadableDetachableModel<File> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
    @SpringBean
    private ContractReportService reportService;

    private long projectId;
    
    private IModel<ContractReportMetaInformation> reportModel;

    public ContractReportFileModel(long projectId, IModel<ContractReportMetaInformation> reportModel) {
        Injector.get().inject(this);
        this.projectId = projectId;
        this.reportModel = reportModel;
    }

    @Override
    protected File load() {
    	return reportService.createReportFile(projectId,reportModel.getObject().getSelectedMonth().getDate());
    }

}
