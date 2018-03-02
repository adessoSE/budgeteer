package org.wickedsource.budgeteer.SheetTemplate;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

public class FlagTemplateTest {

	private FlagTemplate flagTemplate;
	private Sheet sheet;
	
	@Before
	public void setUp() throws Exception {
		InputStream in = new FileInputStream("test-mapping.xlsx");
		Workbook wb = (XSSFWorkbook) WorkbookFactory.create(in);
		sheet = wb.getSheet("Flags");
		flagTemplate = new FlagTemplate(sheet);
	}
	
	@Test
	public void testMappings() {
		assertTrue(flagTemplate.contains("warning1"));
		assertTrue(flagTemplate.contains("warning2"));
		assertTrue(flagTemplate.contains("warning3"));
	}
	
	@Test
	public void testFlagMappings() {
		assertEquals(sheet.getRow(0).getCell(0).getCellStyle(),flagTemplate.getCellStyleFor("warning1"));
		assertEquals(sheet.getRow(0).getCell(1).getCellStyle(),flagTemplate.getCellStyleFor("warning2"));
		assertEquals(sheet.getRow(0).getCell(2).getCellStyle(),flagTemplate.getCellStyleFor("warning3"));
		assertNull(flagTemplate.getCellStyleFor("doesnotexist"));
	}

	@Test
	public void testIsFlagTag() {
		assertTrue(flagTemplate.isFlagTag("{flag:test1}"));
		assertFalse(flagTemplate.isFlagTag("{flag:}"));
		assertFalse(flagTemplate.isFlagTag("{flag}"));
		assertFalse(flagTemplate.isFlagTag("{test}"));
	}

}
