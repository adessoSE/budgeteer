package org.wickedsource.budgeteer.service.contract.report;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplate;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplateSerializable;
import org.wickedsource.budgeteer.SheetTemplate.TemplateWriter;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.service.ReportType;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class ContractReportService {

	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private ContractReportDataMapper mapper;
	
	@Autowired
	private ContractReportMonthlyDataMapper monthlyMapper;

	@Autowired
	private TemplateService templateService;
	
	public File createReportFile(long templateId, long projectId,Date endDate) {
		XSSFWorkbook wb = getSheetWorkbook(templateId);

		// Overal summary
		List<ContractReportData> contractReportList = loadContractReportData(projectId, endDate);
		writeContractData(wb.getSheetAt(0),contractReportList);

		List<ContractReportSummary> summary = createSummary(contractReportList);
		writeSummary(wb.getSheetAt(0), summary,false);

		 // Monthly summary
		List<ContractReportData> monthlyContractReportList = loadMonthlyContractReportData(projectId, endDate);
		writeContractData(wb.getSheetAt(1),monthlyContractReportList);

		List<ContractReportSummary> monthlySummary = createSummary(monthlyContractReportList);
		writeSummary(wb.getSheetAt(1), monthlySummary,true);

		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		return outputfile(wb);
	}

	private void writeSummary(XSSFSheet sheet, List<ContractReportSummary> summary, boolean removeFlagSheet) {
		SheetTemplate template = new SheetTemplate(ContractReportSummary.class, sheet);
		TemplateWriter<ContractReportSummary> tw = new TemplateWriter<>(template);
		tw.setEntries(summary);
		tw.write();
		if(removeFlagSheet) {
            tw.removeFlagSheet();
        }
	}

	private List<ContractReportSummary> createSummary(List<ContractReportData> contractReportList) {
		Set<String> recipients = new HashSet<>();
		contractReportList.forEach(
				contract -> recipients.add(getAttribute("rechnungsempfaenger", contract.getAttributes())));

		List<ContractReportSummary> summary = recipients.stream().map(description -> new ContractReportSummary(description))
				.collect(Collectors.toList());
		return summary;
	}

	private String getAttribute(String string, List<? extends SheetTemplateSerializable> list) {
		if (null == list) {
			return "";
		}
		for (SheetTemplateSerializable listEntry : list) {
			if (listEntry.getName().equals(string)) {
				return listEntry.getValue().toString();
			}
		}
		return "";
	}

	private File outputfile(XSSFWorkbook wb) {
		File outputFile = null;
		FileOutputStream out;
		try {
			outputFile = File.createTempFile("contract-report-", ".xlsx");
			outputFile.deleteOnExit();
			out = new FileOutputStream(outputFile);
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputFile;
	}

	private void writeContractData(XSSFSheet sheet, List<ContractReportData> reportList) {
		SheetTemplate template = new SheetTemplate(ContractReportData.class, sheet);
		TemplateWriter<ContractReportData> tw = new TemplateWriter<>(template);
		tw.setEntries(reportList);
		setWarnings(reportList, tw);
		tw.write();
		
	}

	private void setWarnings(List<ContractReportData> list, TemplateWriter<ContractReportData> tw) {
		list.forEach(contractData -> {
			if (contractData.getProgress() != null &&
					contractData.getProgress() >= 0.6 && contractData.getProgress() < 0.8) {
				tw.addFlag(contractData, "progress", "warning1");
			} else if (contractData.getProgress() != null &&
					contractData.getProgress() >= 0.8 && contractData.getProgress() < 1) {
				tw.addFlag(contractData, "progress", "warning2");
			} else if ((contractData.getProgress() == null && contractData.getBudgetSpent_net() > 0) ||
					(contractData.getProgress() != null && contractData.getProgress() >= 1)) {
				tw.addFlag(contractData, "progress", "warning3");
			}
		});
	}

	private List<ContractReportData> loadContractReportData(long projectId, Date endDate) {
		List<ContractEntity> contracts = new LinkedList<>();
		contracts.addAll(contractRepository.findByProjectId(projectId));
		return mapper.map(contracts,endDate);
	}

	private List<ContractReportData> loadMonthlyContractReportData(long projectId, Date endDate) {
		List<ContractEntity> contracts = new LinkedList<>();
		contracts.addAll(contractRepository.findByProjectId(projectId));
		return monthlyMapper.map(contracts,endDate);
	}

    private XSSFWorkbook getSheetWorkbook(long id) {
        if(id == -1){
            return getDefaultTemplate().getWb();
        }else {
            return templateService.getById(id).getWb();
        }
    }

    public Template getDefaultTemplate(){
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResourceAsStream("contract-report-template.xlsx");
        try {
            return new Template(-1, "Default", "", ReportType.BUDGET_REPORT, (XSSFWorkbook)WorkbookFactory.create(in), -1);
        }catch (InvalidFormatException | IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
