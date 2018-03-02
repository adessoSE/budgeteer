package org.wickedsource.budgeteer.SheetTemplate;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.Multimap;


public class SheetTemplateWriter<T>  {
	
	private static final String FORMAT = "\\{%s\\}";
	private static final Pattern pattern = Pattern.compile("\\{(\\w*)\\}");
	
	private SheetTemplate template;
	private Sheet sheet;
	private List<T> entries;
	private MultiKeyMap<Object,String> flagMapping;

	public SheetTemplateWriter(SheetTemplate sheetTemplate) {
		this.template = sheetTemplate;
		this.sheet = sheetTemplate.getSheet();
		flagMapping = new MultiKeyMap<Object,String>();
	}
	
	public SheetTemplateWriter(SheetTemplate sheetTemplate, List<T> entries) {
		this.template = sheetTemplate;
		this.sheet = sheetTemplate.getSheet();
		this.entries = entries;
		flagMapping = new MultiKeyMap<Object,String>();
	}
	
	public void setEntries(List<T> entries) {
		this.entries = entries;
	}
	
	public void write() {
		if(null == entries || entries.isEmpty()) {
			sheet.removeRow(sheet.getRow(template.getTemplateRowIndex()));
			return;
		} else if (!template.getDtoClass().isInstance(entries.get(0))) {
			throw new IllegalArgumentException("List is empty or wrong data type in list");
		}
		
		// Get Mappings
		Multimap<String, Integer> mapping = template.getFieldMapping();
		
		// Iterate over new rows
		int rowIndex = template.getTemplateRowIndex();
		for (T dto : entries) {
			copyRow(sheet,rowIndex);
			replaceTemplateTags(mapping, dto, sheet.getRow(rowIndex++));
		}
//		 Remove last row
		sheet.removeRow(sheet.getRow(rowIndex));
		if(rowIndex < sheet.getLastRowNum()) {
			sheet.shiftRows(rowIndex+1, sheet.getLastRowNum(), -1);
		}
		
	}

	void replaceTemplateTags(Multimap<String, Integer> mapping, T dto, Row currentRow) {
		// dumm, es genügt über template field zu iterieren. Da sind alle wirklich benötigten enthalten
		for (Field field : template.getDtoClass().getDeclaredFields()) { 
			field.setAccessible(true);
			if (isDynamic(field)) {
				handleDynamicField(mapping, dto, currentRow, field);
			} else {
				handleSimpleField(mapping, dto, currentRow, field);
			}
		}
	}

	void handleDynamicField(Multimap<String, Integer> mapping, T dto, Row currentRow, Field field) {
		String fieldName = null;
		Collection<Integer> columnIndices;
		try {
			List<SheetTemplateSerializable> list = (List) field.get(dto);
			for (SheetTemplateSerializable attribute : list) {
				fieldName = field.getName() + "." + attribute.getName();
				if (mapping.containsKey(fieldName)) {
					columnIndices = mapping.get(field.getName());
					columnIndices.stream()
							.forEach(columnIndex -> replaceTemplateTagInCell(dto, currentRow, field, columnIndex));
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	void handleSimpleField(Multimap<String, Integer> mapping, T dto, Row currentRow, Field field) {
		Collection<Integer> columnIndices;
		if (mapping.containsKey(field.getName())) {
			columnIndices = mapping.get(field.getName());
			columnIndices.stream().forEach(columnIndex -> {
				replaceTemplateTagInCell(dto, currentRow, field, columnIndex);
			});
		}
	}

	boolean isDynamic(Field field) {
		if(field.getType().equals(List.class)) {
			return true;
		}
		return false;
	}

	void replaceTemplateTagInCell(T dto, Row currentRow, Field field, Integer columnIndex) {
		Cell cell = currentRow.getCell(columnIndex);
		try {
			mapCellValue(field.get(dto),field.getName(),cell);
			setFlag(dto,field,cell);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	

	private void setFlag(T dto, Field field, Cell cell) {
		if(flagMapping.containsKey(dto,field.getName())) {
			cell.setCellStyle(sheet.getWorkbook().createCellStyle());
			cell.getCellStyle().cloneStyleFrom(template.getFlagTemplate().getCellStyleFor(flagMapping.get(dto, field.getName())));
		}
	}

	void mapCellValue(Object fieldValue, String fieldname, Cell cell) throws IllegalAccessException {
		boolean isComplexCell = isComplexCell(cell);
		String replaceString = String.format(FORMAT, fieldname);
		
		switch(cell.getCellTypeEnum()) {
			case NUMERIC:
				mapFieldValueToCellValue(fieldValue,cell);
				break;
			case STRING:
				if(isComplexCell) {
					String cellvalue  = cell.getStringCellValue().replaceAll(replaceString, fieldValue.toString());
					cell.setCellValue(cellvalue);
				} else {
					mapFieldValueToCellValue(fieldValue,cell);
				}
				break;
			case BOOLEAN:
				mapFieldValueToCellValue(fieldValue,cell);
				break;
			case FORMULA:
				if(isComplexCell) {
					String cellvalue  = cell.getCellFormula().replaceAll(replaceString, fieldValue.toString());
					cell.setCellFormula(cellvalue);
				} else {
					mapFieldValueToCellValue(fieldValue,cell);
				}
			default:
				break;
		}
	}
	
	void copyRow(Sheet sheet, int from) {
		if(from < sheet.getLastRowNum()) {
			sheet.shiftRows(from+1, sheet.getLastRowNum(), 1);
		}
		Row copyRow = sheet.getRow(from);
		Row insertRow = sheet.createRow(from+1);
		copyRow.cellIterator().forEachRemaining(copyCell -> {
			Cell insertCell = insertRow.createCell(copyCell.getColumnIndex());
			copyCellValues(copyCell,insertCell);
			insertCell.setCellStyle(copyCell.getCellStyle());
		});
	}

	void copyCellValues(Cell copyCell, Cell insertCell) {
		switch (copyCell.getCellTypeEnum()) {
		case STRING:
			insertCell.setCellValue(copyCell.getStringCellValue());
			break;
		case BOOLEAN:
			insertCell.setCellValue(copyCell.getBooleanCellValue());
			break;
		case BLANK:
			insertCell.setCellType(CellType.BLANK);
			break;
		case FORMULA:
			insertCell.setCellFormula(copyCell.getCellFormula());
			break;
		case NUMERIC:
			insertCell.setCellValue(copyCell.getNumericCellValue());
			break;
		default:
			throw new IllegalArgumentException("Unknown Type");
		}
	}
	
	boolean isComplexCell(Cell cell) {
		if(cell == null) {
			return true;
		}
		Matcher matcher = null;
		if(cell.getCellTypeEnum().equals(CellType.FORMULA)) {
			matcher = pattern.matcher(cell.getCellFormula()); // should be false everytime
		} else if(cell.getCellTypeEnum().equals(CellType.STRING)) {
			matcher = pattern.matcher(cell.getStringCellValue());
		} else {
			return true;
		}
		return !matcher.matches();
	}
	
	void mapFieldValueToCellValue(Object fieldValue, Cell cell) throws IllegalAccessException {
		if(null == fieldValue) {
			cell.setCellType(CellType.BLANK);
			return;
		}
		if (Double.class.isInstance(fieldValue)) {
			cell.setCellValue(Double.class.cast(fieldValue));
		} else if (String.class.isInstance(fieldValue)) {
			cell.setCellValue(String.class.cast(fieldValue));
		} else if (Boolean.class.isInstance(fieldValue)) {
			cell.setCellValue(Boolean.class.cast(fieldValue));
		} else if (Date.class.isInstance(fieldValue)) {
			cell.setCellValue(Date.class.cast(fieldValue));
		} else if (RichTextString.class.isInstance(fieldValue)) {
			cell.setCellValue(RichTextString.class.cast(fieldValue));
		} else if (Calendar.class.isInstance(fieldValue)) {
			cell.setCellValue(Calendar.class.cast(fieldValue));
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public void addFlag(T dto, String fieldname, String flag) {
		if(template.getFieldList().contains(fieldname) && null != template.getFlagTemplate() && template.getFlagTemplate().contains(flag)) {
			flagMapping.put(dto, fieldname, flag);
		} else {
			// TODO: Exception
		}
	}
	
	public void removeFlagSheet() {
		Sheet flagSheet = sheet.getWorkbook().getSheet("Flags");
		if(null != flagSheet) {
			int sheetIndex = sheet.getWorkbook().getSheetIndex(flagSheet);
			sheet.getWorkbook().removeSheetAt(sheetIndex);
		}
	}

}
