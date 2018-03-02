package org.wickedsource.budgeteer.SheetTemplate;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;


public class SheetTemplateWriterTest {
	
	private Workbook wb;
	private Sheet sheet;
	private SheetTemplate template;
	private TestDTO dto1,dto2;
	
	@Before
	public void setUp() throws EncryptedDocumentException, InvalidFormatException, IOException {
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
	}
	
	@SuppressWarnings("unused")
	@Test
	public void init() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<TestDTO>(template);
	}

	@Test
	public void testIsComplexCell() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<TestDTO>(template);
		assertFalse(stw.isComplexCell(sheet.getRow(4).getCell(0)));
		assertTrue(stw.isComplexCell(sheet.getRow(4).getCell(1)));
	}
	
	@Test
	public void testCopyRowAndIsLastRow() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<TestDTO>(template);
		stw.copyRow(sheet, 4);

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
	public void testCopyRowAndIsNotLastRow() {
		sheet.createRow(8).createCell(9).setCellValue("Test 123");
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<TestDTO>(new SheetTemplate(TestDTO.class, sheet));
		stw.copyRow(sheet, 4);

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
	
//	@Test
//	public void testFieldValueToCellValue() throws IllegalAccessException, NoSuchFieldException, SecurityException {
//		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<SheetTemplateWriterTest.TestDTO>(template);
//
//		Date now = new Date();
//		dto1.setDate(now);
//		
//		Field[] fields = dto1.getClass().getDeclaredFields();
//		Arrays.stream(fields).forEach(field -> field.setAccessible(true));
//
//		Cell cell = null;
//		
//		cell = sheet.getRow(4).getCell(4);
//		stw.mapFieldValueToCellValue(dto1, fields[3], cell);
//		assertEquals(CellType.NUMERIC,cell.getCellTypeEnum());
//		assertEquals(now,cell.getDateCellValue());
//		
//		cell = sheet.getRow(4).getCell(18);
//		stw.mapFieldValueToCellValue(dto1, fields[1], cell);
//		assertEquals(CellType.NUMERIC, cell.getCellTypeEnum());
//		assertEquals(123.4567899,cell.getNumericCellValue(),10e-8);
//		
//		cell = sheet.getRow(4).getCell(2);
//		stw.mapFieldValueToCellValue(dto1, fields[2],cell);
//		assertEquals(CellType.BOOLEAN, cell.getCellTypeEnum());
//		assertEquals(true,cell.getBooleanCellValue());
//		
//		cell = sheet.getRow(4).getCell(0);
//		stw.mapFieldValueToCellValue(dto1, fields[0], cell);
//		assertEquals(CellType.STRING, cell.getCellTypeEnum());
//		assertEquals("Foo",cell.getStringCellValue());
//	}
	
	@Test
	public void testReplaceTemplateTags() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<TestDTO>(template);
		Row row = sheet.getRow(4);
		stw.replaceTemplateTags(template.getFieldMapping(), dto1, row);
		assertEquals("Foo - 123.4567899",row.getCell(1).getStringCellValue());
	}
	
	@Test
	public void testWriteDataIntoSheet() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<TestDTO>(template);
		stw.setEntries(Arrays.asList(dto1,dto2));
		stw.write();
		
		Row row = sheet.getRow(4);
		assertEquals("Foo - 123.4567899",row.getCell(1).getStringCellValue());
		assertEquals("Max",row.getCell(6));
		assertEquals("Mustermann",row.getCell(7));
		assertEquals("Mustermann, Max",row.getCell(8));
		
		row = sheet.getRow(5);
		assertEquals("Bar - 987.654321",row.getCell(1).getStringCellValue());
		assertEquals("Marina",row.getCell(6));
		assertEquals("Musterfrau",row.getCell(7));
		assertEquals("Musterfrau, Marina",row.getCell(8));
	}
	
	@Test
	public void testWriteDataIntoSheetAndListIsEmpty() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<TestDTO>(template);
		stw.setEntries(new ArrayList<TestDTO>());
		stw.write();
		assertTrue(rowIsEmpty(sheet.getRow(4)));
	}
	
	@Test
	public void testFlagTemplate() throws EncryptedDocumentException, InvalidFormatException, IOException {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<TestDTO>(template);
		stw.setEntries(Arrays.asList(dto1,dto2));
		stw.addFlag(dto2, "test", "warning1");
		
		stw.write();
		
		CellStyle expected = wb.getSheet("Flags").getRow(0).getCell(0).getCellStyle();
		CellStyle was = sheet.getRow(4).getCell(0).getCellStyle();
		assertEquals(expected,was);
	}
	
	@Test
	public void testIsDynamic() throws NoSuchFieldException, SecurityException {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<TestDTO>(template);
		Field dynamicField = TestDTO.class.getDeclaredField("dynamic");
		assertTrue(stw.isDynamic(dynamicField));
		
		Field noDynamicField = TestDTO.class.getDeclaredField("foo");
		assertFalse(stw.isDynamic(noDynamicField));
	}
	
	private boolean rowIsEmpty(Row row) {
	    if (row == null) {
	        return true;
	    }
	    if (row.getLastCellNum() <= 0) {
	        return true;
	    }
	    for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
	        Cell cell = row.getCell(cellNum);
	        if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && !cell.toString().equals("")) {
	            return false;
	        }
	    }
	    return true;
	}
	
}
