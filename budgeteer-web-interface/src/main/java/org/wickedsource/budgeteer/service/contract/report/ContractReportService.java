package org.wickedsource.budgeteer.service.contract.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.poi.EncryptedDocumentException;
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
import org.wickedsource.budgeteer.service.budget.report.BudgetSummary;

@Transactional
@Service
public class ContractReportService {

	@Autowired
	private ContractRepository contractRepository;
	
	@Autowired
	private ContractReportDataMapper mapper;
	
	public File createReportFile(long projectId) {
		XSSFWorkbook wb = getSheetWorkbook();

		List<ContractReportData> contractReportList = loadContractReportData(projectId);
		writeContractData(wb.getSheetAt(0),contractReportList);

		List<ContractReportSummary> summary = createBudgetSummary(contractReportList);
		writeSummary(wb.getSheetAt(0), summary);

		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		return outputfile(wb);
	}
	
	private void writeSummary(XSSFSheet sheet, List<ContractReportSummary> summary) {
		SheetTemplate template = new SheetTemplate(BudgetSummary.class, sheet);
		TemplateWriter<ContractReportSummary> tw = new TemplateWriter<ContractReportSummary>(template);
		tw.setEntries(summary);
		tw.write();
		tw.removeFlagSheet();
	}

	private List<ContractReportSummary> createBudgetSummary(List<ContractReportData> contractReportList) {
		Set<String> recipients = new HashSet<String>();
		contractReportList.stream().forEach(
				contract -> recipients.add((String) getAttribute("rechnungsempfaenger", contract.getAttributes())));

		List<ContractReportSummary> summary = recipients.stream().map(description -> new ContractReportSummary(description))
				.collect(Collectors.toList());
		return summary;
	}

	private Object getAttribute(String string, List<? extends SheetTemplateSerializable> list) {
		if (null == list) {
			return null;
		}
		for (SheetTemplateSerializable listEntry : list) {
			if (listEntry.getName().equals(string)) {
				return listEntry.getValue();
			}
		}
		return null;
	}

	private File outputfile(XSSFWorkbook wb) {
		File outputFile = null;
		FileOutputStream out = null;
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

	private void writeContractData(XSSFSheet sheet, List<ContractReportData> monthlyBudgetReportList) {
		SheetTemplate template = new SheetTemplate(ContractReportData.class, sheet);
		TemplateWriter<ContractReportData> tw = new TemplateWriter<ContractReportData>(template);
		tw.setEntries(monthlyBudgetReportList);
		setWarnings(monthlyBudgetReportList, tw);
		tw.write();
		
	}

	private void setWarnings(List<ContractReportData> budgetList, TemplateWriter<ContractReportData> tw) {
		budgetList.stream().forEach(budgetData -> {
			if (budgetData.getProgress() >= 0.6 && budgetData.getProgress() < 0.8) {
				tw.addFlag(budgetData, "progress", "warning1");
			} else if (budgetData.getProgress() >= 0.8 && budgetData.getProgress() < 1) {
				tw.addFlag(budgetData, "progress", "warning2");
			} else if (budgetData.getProgress() >= 1) {
				tw.addFlag(budgetData, "progress", "warning3");
			}
		});
	}

	private List<ContractReportData> loadContractReportData(long projectId) {
		List<ContractEntity> contracts = new LinkedList<ContractEntity>();
		contracts.addAll(contractRepository.findByProjectId(projectId));
		return mapper.map(contracts);
	}

	private XSSFWorkbook getSheetWorkbook() {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream("contract-report-template.xlsx");
		XSSFWorkbook wb = null;
		try {
			wb = (XSSFWorkbook) WorkbookFactory.create(in);
		} catch (EncryptedDocumentException | IOException | InvalidFormatException e) {
			e.printStackTrace();
		}
		return wb;
	}
}
