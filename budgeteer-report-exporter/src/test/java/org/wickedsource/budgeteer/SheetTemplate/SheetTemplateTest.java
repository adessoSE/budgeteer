package org.wickedsource.budgeteer.SheetTemplate;

import com.google.common.collect.Multimap;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


public class SheetTemplateTest {
	
	private Workbook wb;
	private Sheet sheet;
	private CellStyle testStyle;
	private SheetTemplate st;
	
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
		templateRow.createCell(18).setCellValue("{dynamic.Bescha-Nr.}");
		templateRow.createCell(4).setCellValue(1235.123456);
		
		
		templateRow.createCell(100).setCellValue("hallo: {foo}");

		st = new SheetTemplate(TestDTO.class, sheet);
	}
	
	@Test
	public void init() {
	}
	
	@Test
	public void testGetCellMapping() {
		Multimap<String,Integer> mapping = st.getFieldMapping();
		assertNotNull(mapping);
		
		assertTrue(mapping.containsKey("test"));
		assertTrue(mapping.get("test").contains(5));
		assertTrue(mapping.containsKey("bar"));
		assertTrue(mapping.get("bar").contains(3));
		assertTrue(mapping.containsKey("foo"));
		assertTrue(mapping.get("foo").contains(100));
		assertTrue(mapping.get("foo").contains(3));
		assertTrue(mapping.get("dynamic.Bescha-Nr.").contains(18));
		assertTrue(mapping.get("dynamic.name").contains(15));
	}
	
	@Test
	public void testCellContainsTemplateTagAndNotContainsTag() {
		assertFalse(st.cellContainsTemplateTag(sheet.getRow(4).getCell(2)));
	}
	
	@Test
	public void testDotInField() {
		assertTrue(st.cellContainsTemplateTag(sheet.getRow(4).getCell(15)));
		assertFalse(st.cellContainsTemplateTag(sheet.getRow(4).getCell(16)));
		assertFalse(st.cellContainsTemplateTag(sheet.getRow(4).getCell(17)));
	}
	
	@Test
	public void testCellContainsTemplateTagAndContainsSimpleTag() {
		assertTrue(st.cellContainsTemplateTag(sheet.getRow(4).getCell(5)));
	}
	
	@Test
	public void testCellContainsTemplateTagAndContainsComplexTag() {
		assertTrue(st.cellContainsTemplateTag(sheet.getRow(4).getCell(100)));
	}
	
	@Test
	public void testCellContainsTemplateTagAndContainsMultipleTags() {
		assertTrue(st.cellContainsTemplateTag(sheet.getRow(4).getCell(100)));
		assertTrue(st.cellContainsTemplateTag(sheet.getRow(4).getCell(3)));
	}
	
	@Test
	public void testFindTemplateRow() {
		assertEquals(4,st.getTemplateRowIndex());
	}

	@Test
	public void testmapCellValueToFieldNamesWithDynamicAttribute() {
		Cell cellMock = Mockito.mock(Cell.class);
		when(cellMock.getCellTypeEnum()).thenReturn(CellType.STRING);
		when(cellMock.getStringCellValue()).thenReturn("{dynamic.Bescha-Nr.}"); // valid

		List<String> fields = st.mapCellValueToFieldNames(cellMock);

		Assertions.assertThat(fields).contains("dynamic.Bescha-Nr.");
	}

	@Test
	public void testmapCellValueToFieldNamesWithDynamicAttributeAndNoSpecials() {
		Cell cellMock = Mockito.mock(Cell.class);
		when(cellMock.getCellTypeEnum()).thenReturn(CellType.STRING);
		when(cellMock.getStringCellValue()).thenReturn("{dynamic.Bescha-Nr}"); //  valid

		List<String> fields = st.mapCellValueToFieldNames(cellMock);

		Assertions.assertThat(fields).contains("dynamic.Bescha-Nr");
	}


	@Test
	public void testmapCellValueToFieldNamesWithRawValue() {
		Cell cellMock = Mockito.mock(Cell.class);
		when(cellMock.getCellTypeEnum()).thenReturn(CellType.STRING);
		when(cellMock.getStringCellValue()).thenReturn("{dynamic}"); // valid
		List<String> fields = st.mapCellValueToFieldNames(cellMock);
		Assertions.assertThat(fields).contains("dynamic");
	}


	@Test
	public void testmapCellValueToFieldNamesWithStartingDot() {
		Cell cellMock = Mockito.mock(Cell.class);
		when(cellMock.getCellTypeEnum()).thenReturn(CellType.STRING);
		when(cellMock.getStringCellValue()).thenReturn("{.test}"); // not valid

		List<String> fields = st.mapCellValueToFieldNames(cellMock);

		Assertions.assertThat(fields).isEmpty();
	}


	@Test
	public void testmapCellValueToFieldNamesWithMultipleFields() {
		Cell cellMock = Mockito.mock(Cell.class);
		when(cellMock.getCellTypeEnum()).thenReturn(CellType.STRING);
		when(cellMock.getStringCellValue()).thenReturn("{test} - {dynamic.name}"); // not valid

		List<String> fields = st.mapCellValueToFieldNames(cellMock);

		Assertions.assertThat(fields).hasSize(2)
				.contains("test")
				.contains("dynamic.name");
	}


	@Test
	public void testmapCellValueToFieldNamesWithMultipleDynamicFields() {
		st = spy(st);
		when(st.dtoHasField("dynamic")).thenReturn(true);
		when(st.dtoHasField("veryDynamic")).thenReturn(true);

		Cell cellMock = Mockito.mock(Cell.class);
		when(cellMock.getCellTypeEnum()).thenReturn(CellType.STRING);
		when(cellMock.getStringCellValue()).thenReturn("{dynamic.test} - {veryDynamic.attribute}"); // not valid

		List<String> fields = st.mapCellValueToFieldNames(cellMock);

		Assertions.assertThat(fields).hasSize(2)
				.contains("dynamic.test")
				.contains("veryDynamic.attribute");
	}

	@Test
	public void testmapCellValueToFieldNamesWithUnderscore() {
		st = spy(st);
		when(st.dtoHasField("test_name")).thenReturn(true);
		Cell cellMock = Mockito.mock(Cell.class);
		when(cellMock.getCellTypeEnum()).thenReturn(CellType.STRING);
		when(cellMock.getStringCellValue()).thenReturn("{test_name}"); // not valid

		List<String> fields = st.mapCellValueToFieldNames(cellMock);

		Assertions.assertThat(fields).hasSize(1)
				.contains("test_name");
	}
}
