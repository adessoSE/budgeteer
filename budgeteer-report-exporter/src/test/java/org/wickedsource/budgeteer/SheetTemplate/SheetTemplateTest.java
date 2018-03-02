package org.wickedsource.budgeteer.SheetTemplate;
import static org.junit.Assert.*;

import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Multimap;


public class SheetTemplateTest {
	
	private Workbook wb;
	private Sheet sheet;
	private CellStyle testStyle;
	
	public class TestDTO {
		private String test;
		private double foo;
		private double bar;
		private List<Attribute> dynamic;
		public String getTest() {
			return test;
		}
		public void setTest(String test) {
			this.test = test;
		}
		public double getFoo() {
			return foo;
		}
		public void setFoo(double foo) {
			this.foo = foo;
		}
		public double getBar() {
			return bar;
		}
		public void setBar(double bar) {
			this.bar = bar;
		}
		public List<Attribute> getDynamic() {
			return dynamic;
		}
		public void setDynamic(List<Attribute> dynamic) {
			this.dynamic = dynamic;
		}
		
	}
	
	@Before
	public void setUp() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet();
		Row templateRow = sheet.createRow(4);
		templateRow.createCell(2).setCellValue("test");
		
		templateRow.createCell(5).setCellValue("{test}");
		testStyle = templateRow.getCell(5).getCellStyle();
		testStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());

		templateRow.createCell(3).setCellValue("{foo} - {bar}");
		templateRow.createCell(15).setCellValue("{dynamic.name}");
		templateRow.createCell(16).setCellValue("{.name}");
		templateRow.createCell(17).setCellValue("{notexisting.name}");
		templateRow.createCell(4).setCellValue(1235.123456);
		
		
		templateRow.createCell(100).setCellValue("hallo: {foo}");
	}
	
	@Test
	public void init() {
		new SheetTemplate(TestDTO.class, sheet);
	}
	
	@Test
	public void testGetCellMapping() {
		SheetTemplate st = new SheetTemplate(TestDTO.class, sheet);
		Multimap<String,Integer> mapping = st.getFieldMapping();
		assertNotNull(mapping);
		
		assertTrue(mapping.containsKey("test"));
		assertTrue(mapping.get("test").contains(5));
		assertTrue(mapping.containsKey("bar"));
		assertTrue(mapping.get("bar").contains(3));
		assertTrue(mapping.containsKey("foo"));
		assertTrue(mapping.get("foo").contains(100));
		assertTrue(mapping.get("foo").contains(3));
		assertTrue(mapping.get("dynamic.name").contains(15));
	}
	
	@Test
	public void testCellContainsTemplateTagAndNotContainsTag() {
		SheetTemplate st = new SheetTemplate(TestDTO.class, sheet);
		assertFalse(st.cellContainsTemplateTag(sheet.getRow(4).getCell(2)));
	}
	
	@Test
	public void testDotInField() {
		SheetTemplate st = new SheetTemplate(TestDTO.class, sheet);
		assertTrue(st.cellContainsTemplateTag(sheet.getRow(4).getCell(15)));
		assertFalse(st.cellContainsTemplateTag(sheet.getRow(4).getCell(16)));
		assertFalse(st.cellContainsTemplateTag(sheet.getRow(4).getCell(17)));
	}
	
	@Test
	public void testCellContainsTemplateTagAndContainsSimpleTag() {
		SheetTemplate st = new SheetTemplate(TestDTO.class, sheet);
		assertTrue(st.cellContainsTemplateTag(sheet.getRow(4).getCell(5)));
	}
	
	@Test
	public void testCellContainsTemplateTagAndContainsComplexTag() {
		SheetTemplate st = new SheetTemplate(TestDTO.class, sheet);
		assertTrue(st.cellContainsTemplateTag(sheet.getRow(4).getCell(100)));
	}
	
	@Test
	public void testCellContainsTemplateTagAndContainsMultipleTags() {
		SheetTemplate st = new SheetTemplate(TestDTO.class, sheet);
		assertTrue(st.cellContainsTemplateTag(sheet.getRow(4).getCell(100)));
		assertTrue(st.cellContainsTemplateTag(sheet.getRow(4).getCell(3)));
	}
	
	@Test
	public void testFindTemplateRow() {
		SheetTemplate st = new SheetTemplate(TestDTO.class, sheet);
		assertEquals(4,st.getTemplateRowIndex());
	}

	// TODO: Add Test for Formula
}
