package org.wickedsource.budgeteer.web.pages.contract.overview.report.form;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
    	LocalDate now = LocalDate.now();
    	LocalDate adjustedDate = reportModel.getObject().getSelectedMonth().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    			.plus(1,ChronoUnit.MONTHS).minus(1,ChronoUnit.DAYS);
    	Date endDate = null;
    	if(now.isBefore(adjustedDate)) {
    		endDate = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	} else {
    		endDate = Date.from(adjustedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    	}
    	
    	return reportService.createReportFile(projectId,endDate);
    }

}
