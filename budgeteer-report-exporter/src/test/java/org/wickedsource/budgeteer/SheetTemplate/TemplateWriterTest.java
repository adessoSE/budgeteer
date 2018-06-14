package org.wickedsource.budgeteer.SheetTemplate;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;

class TemplateWriterTest {

	private XSSFWorkbook wb;
	private TemplateWriter<TestDTO> tw;
	private Sheet sheet;
	private SheetTemplate template;
	private TestDTO dto1,dto2;
	
	@BeforeEach
	void setUp() throws Exception {
		InputStream in = new FileInputStream("test-mapping.xlsx");
		wb = (XSSFWorkbook) WorkbookFactory.create(in);
		sheet = wb.getSheetAt(0);
		template = new SheetTemplate(TestDTO.class, sheet);
		dto1 = new TestDTO();
		dto1.setBar(true);
		dto1.setFoo(123.4567899);
		dto1.setTest("Foo");
		dto1.setDate(new Date());
		dto1.setDynamic(Arrays.asList(new Attribute("vorname", "Max"), new Attribute("nachname", "Mustermann")));
		
		dto2 = new TestDTO();
		dto2.setBar(false);
		dto2.setFoo(987.654321);
		dto2.setTest("Bar");
		dto2.setDate(new Date());
		dto2.setDynamic(Arrays.asList(new Attribute("vorname", "Marina"), new Attribute("nachname","Musterfrau")));
		tw = new TemplateWriter<TestDTO>(template);
	}

	@Test
	void testWrite() {
		tw.setEntries(Arrays.asList(dto1,dto2));
		tw.write();
		
		Row row = sheet.getRow(4);
		assertEquals("Foo",row.getCell(0).getStringCellValue());
		assertEquals("Foo - 123.4567899",row.getCell(1).getStringCellValue());
		assertTrue(row.getCell(2).getBooleanCellValue());
		assertEquals("",row.getCell(3).getStringCellValue());
		// date
		// calendar
		assertEquals("Max",row.getCell(6).getStringCellValue());
		assertEquals("Mustermann",row.getCell(7).getStringCellValue());
		assertEquals("Mustermann, Max",row.getCell(8).getStringCellValue());
		
		row = sheet.getRow(5);
		assertEquals("Bar",row.getCell(0).getStringCellValue());
		assertEquals("Bar - 987.654321",row.getCell(1).getStringCellValue());
		assertFalse(row.getCell(2).getBooleanCellValue());
		assertEquals("",row.getCell(3).getStringCellValue());
		// date
		// calendar
		assertEquals("Marina",row.getCell(6).getStringCellValue());
		assertEquals("Musterfrau",row.getCell(7).getStringCellValue());
		assertEquals("Musterfrau, Marina",row.getCell(8).getStringCellValue());
	}

	@Test
	void testInsertZeroRows() {
		int lastRowNumber = sheet.getLastRowNum();
		tw.setEntries(Collections.nCopies(0, new TestDTO()));
		tw.insertRows();
		assertEquals(lastRowNumber-1,sheet.getLastRowNum());
	}

	@Test
	void testInsertOneRow() {
		int lastRowNumber = sheet.getLastRowNum();
		tw.setEntries(Collections.nCopies(1, new TestDTO()));
		tw.insertRows();
		assertEquals(lastRowNumber,sheet.getLastRowNum());
	}

	@Test
	void testInsertMultipleRows() {
		int lastRowNumber = sheet.getLastRowNum();
		tw.setEntries(Collections.nCopies(5, new TestDTO()));
		tw.insertRows();
		assertEquals(lastRowNumber+4,sheet.getLastRowNum());
	}

	@Test
	void testCopyRow() {
		tw.copyRow(sheet, 4);

		Row copiedRow = sheet.getRow(5);
		assertNotNull(copiedRow);
		
		for(Cell cell : sheet.getRow(4)) {
			assertEquals(cell.getCellTypeEnum(),cell.getCellTypeEnum());
			switch(cell.getCellTypeEnum()) {
			case NUMERIC:
				assertEquals(cell.getDateCellValue(), copiedRow.getCell(cell.getColumnIndex()).getDateCellValue());
				assertEquals(cell.getNumericCellValue(), copiedRow.getCell(cell.getColumnIndex()).getNumericCellValue(),10e-8);
				break;
			case STRING:
				assertEquals(cell.getStringCellValue(),copiedRow.getCell(cell.getColumnIndex()).getStringCellValue());
				break;
			case BOOLEAN:
				assertEquals(cell.getBooleanCellValue(), copiedRow.getCell(cell.getColumnIndex()).getBooleanCellValue());
				break;
			case FORMULA:
				assertEquals(cell.getCellFormula(), copiedRow.getCell(cell.getColumnIndex()).getCellFormula());
				break;
			default:
				fail("Not supported type // unknown type");
				break;
			}
		}
	}
	
	@Test
	void testSetFlag() {
		tw.setEntries(Arrays.asList(dto1,dto2));
		tw.addFlag(dto1, "dynamic.vorname" , "warning1");
		tw.write();
		
		Row row = sheet.getRow(4);
		Cell cell = row.getCell(6);
		CellStyle style = template.getFlagTemplate().getCellStyleFor("warning1");
		assertEquals(style,cell.getCellStyle());

		cell = row.getCell(8);
		style = template.getFlagTemplate().getCellStyleFor("warning1");
		assertEquals(style,cell.getCellStyle());
		
	}

	@Test
	void testRemoveFlagSheet() {
		tw.removeFlagSheet();
		Sheet removedSheet = wb.getSheet("Flags");
		assertNull(removedSheet);
	}
	
	@Test
	void testIsDynamicField() {
		assertTrue(tw.isDynamicField("foo.bar"));
		assertFalse(tw.isDynamicField("foo"));
	}

	@Test
	void testSubkeyOfWithDotAtTheEnd() {
		String result = tw.subkeyOf("test.name.");
		assertEquals("name.", result);
	}

	@Test
	void testSubkeyOfWithMultipleDots() {
		String result = tw.subkeyOf("test.name.multiple.test");
		assertEquals("name.multiple.test", result);
	}

	@Test
	void testSubkeyOfWithNoDots() {
		String result = tw.subkeyOf("test");
		assertNull(result);
	}
}
