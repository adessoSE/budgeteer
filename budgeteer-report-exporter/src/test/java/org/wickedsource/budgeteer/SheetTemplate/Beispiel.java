package org.wickedsource.budgeteer.SheetTemplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Beispiel {
	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
		InputStream in = new FileInputStream("test-mapping.xlsx");
		Workbook wb = (XSSFWorkbook) WorkbookFactory.create(in);
		
		
		
		SheetTemplate template = new SheetTemplate(TestDTO.class, wb.getSheetAt(0));
		TemplateWriter<TestDTO> tw = new TemplateWriter<TestDTO>(template);
		

		TestDTO dto1 = new TestDTO();
		dto1.setBar(true);
		dto1.setFoo(123.4567899);
		dto1.setTest("Foo");
		dto1.setDate(new Date());
		dto1.setDynamic(Arrays.asList(new Attribute("vorname", "Max"), new Attribute("nachname", "Mustermann")));
		
		TestDTO dto2 = new TestDTO();
		dto2.setBar(false);
		dto2.setFoo(987.654321);
		dto2.setTest("Bar");
		dto2.setDate(new Date());
		dto2.setDynamic(Arrays.asList(new Attribute("vorname", "Marina"), new Attribute("nachname","Musterfrau")));
		
		tw.setEntries(Arrays.asList(dto1,dto2));
		tw.write();
		
		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		
		OutputStream out = new FileOutputStream("test-output.xlsx");
		wb.write(out);
	}
}
