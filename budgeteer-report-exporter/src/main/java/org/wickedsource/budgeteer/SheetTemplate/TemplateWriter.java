package org.wickedsource.budgeteer.SheetTemplate;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;

public class TemplateWriter<T> {
	
	private static final String TEMPLATE_TAG_FORMAT = "\\{%s\\}";
	
	private SheetTemplate template;
	private Sheet sheet;
	private List<T> entries;
	private Multimap<T,FieldFlag> flagMapping;
	
	
	private int currentRowIndex;
	private Row currentRow;

	public TemplateWriter(SheetTemplate sheetTemplate) {
		this.template = sheetTemplate;
		this.sheet = sheetTemplate.getSheet();
		flagMapping = ArrayListMultimap.create();
	}
	
	public TemplateWriter(SheetTemplate sheetTemplate, List<T> entries) {
		this.template = sheetTemplate;
		this.sheet = sheetTemplate.getSheet();
		this.entries = entries;
		flagMapping = ArrayListMultimap.create();
	}
	
	public void setEntries(List<T> entries) {
		this.entries = entries;
	}
	
	public void write() {
		insertRows(); // insert Rows with template tags
		if(null != entries && !entries.isEmpty()) {
			currentRowIndex = template.getTemplateRowIndex();
			entries.stream().forEach(dto -> insert(dto));
		}
	}

	void insert(T dto) {
		currentRow = sheet.getRow(currentRowIndex);
		replaceTemplateTags(dto);
		setCellStyle(dto);
		currentRowIndex++;
	}

	@SuppressWarnings("unchecked")
	private void replaceTemplateTags(T dto) {
		Multimap<String, Integer> fieldMapping = template.getFieldMapping();

		for (Entry<String, Integer> entry : fieldMapping.entries()) {
			Cell currentCell = currentRow.getCell(entry.getValue());
			String tagname = entry.getKey();

			Object fieldValue = null;
			try {
				if (isDynamicField(tagname)) {
					Field field = dto.getClass().getDeclaredField(getFieldnameOf(tagname));
					field.setAccessible(true);
					Object fieldObject = field.get(dto);
					if(fieldObject instanceof List) {
						List<SheetTemplateSerializable> listObject = (List<SheetTemplateSerializable>) fieldObject;
						HashMap<String,Object> map = new HashMap<String,Object>();
						listObject.stream().forEach(listEntry -> map.put(listEntry.getName(), listEntry.getValue()));
						fieldValue = map.get(subkeyOf(tagname));
					} else if(fieldObject instanceof Map) {
						Map<String,Object> mapObject = (Map<String, Object>) fieldObject;
						fieldValue = mapObject.get(subkeyOf(tagname));
					} else if (fieldObject == null) {
						// do nothing
					} else {
						throw new IllegalArgumentException("The field " + field.getName() + " uses an unsupported type.");
					}
				} else {
					Field field = dto.getClass().getDeclaredField(tagname);
					field.setAccessible(true);
					fieldValue = field.get(dto);
				}
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			replaceTemplateTagByFieldValueIntoCell(tagname, fieldValue, currentCell);
		}
	}

	private void replaceTemplateTagByFieldValueIntoCell(String fieldname, Object fieldValue, Cell currentCell) {
		if(containsOnlyOneTemplateTag(currentCell)) {
			mapFieldValueToCell(fieldValue,currentCell);
		} else {
			replaceTemplateTagInCell(fieldname,fieldValue,currentCell);
		}
	}

	private void replaceTemplateTagInCell(String fieldname, Object fieldValue, Cell currentCell) {
		String fieldValueString = (null != fieldValue) ? fieldValue.toString() : "";
		if(currentCell.getCellTypeEnum().equals(CellType.FORMULA)) {
			String formula = currentCell.getCellFormula();
			String templateTag = String.format(TEMPLATE_TAG_FORMAT, fieldname);
			String newFormula = formula.replaceAll(templateTag, fieldValueString);
			currentCell.setCellFormula(newFormula);
		} else {
			String cellValue= currentCell.getStringCellValue();
			String templateTag = String.format(TEMPLATE_TAG_FORMAT, fieldname);
			String newCellValue = cellValue.replaceAll(templateTag, fieldValueString);
			currentCell.setCellValue(newCellValue);
		}
		
	}

	private void mapFieldValueToCell(Object fieldValue, Cell cell) {
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

	boolean containsOnlyOneTemplateTag(Cell currentCell) {
		try {
			Matcher matcher = SheetTemplate.TEMPLATE_TAG_PATTERN.matcher(currentCell.getStringCellValue());
			return matcher.matches();
		} catch(Exception e) {
			return false;
		}
	}

	boolean isDynamicField(String fieldname) {
		String[] tokens = fieldname.split("\\.");
		return tokens.length > 1;
	}
	
	String getFieldnameOf(String dynamicField) {
		String[] tokens = dynamicField.split("\\.");
		return tokens[0];
	}

	private Object subkeyOf(String tagname) {
		String[] tokens = tagname.split("\\.");
		return tokens[1];
	}

	void insertRows() {
		// determine number of rows to be inserted
		int numberOfRows;
		if(null != entries) {
			numberOfRows = entries.size();
		} else {
			numberOfRows = 0;
		}
		
		boolean templateRowIsLastRow = (template.getTemplateRowIndex() == sheet.getLastRowNum());
		
		if(!templateRowIsLastRow && numberOfRows == 0) { // if we do not have data, we have to remove the template row
			sheet.removeRow(sheet.getRow(template.getTemplateRowIndex()));
			sheet.shiftRows(template.getTemplateRowIndex(), sheet.getLastRowNum(), -1);
		} 
		
		// copy template row numberOfRows-1 times
		for(int i = 0; i < numberOfRows-1; i++) {
			copyRow(sheet,template.getTemplateRowIndex()+i);
		}
	}
	
	void copyRow(Sheet sheet, int from) {
		if(from < sheet.getLastRowNum()) {
			sheet.shiftRows(from+1, sheet.getLastRowNum(), 1);
		}
		Row copyRow = sheet.getRow(from);
		Row insertRow = sheet.createRow(from+1);
		for(Cell copyCell : copyRow) {
			Cell insertCell = insertRow.createCell(copyCell.getColumnIndex());
			copyCellValues(copyCell,insertCell);
			insertCell.setCellStyle(copyCell.getCellStyle());
		}
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
			throw new IllegalArgumentException("Unknown Type"); // should not occure
		}
	}

	void setCellStyle(T dto) {
		if(flagMapping.containsKey(dto)) {
			for(FieldFlag flag : flagMapping.get(dto)) {
				String fieldname = flag.getField();
				String flagname = flag.getFlag();
				for(Integer columnIndex : template.getFieldMapping().get(fieldname)) {
					Cell cell = currentRow.getCell(columnIndex);
					cell.setCellStyle(sheet.getWorkbook().createCellStyle());
					cell.getCellStyle().cloneStyleFrom(template.getFlagTemplate().getCellStyleFor(flagname));
				}
			}
		}
	}
	
	public void addFlag(T dto, String fieldname, String flag) {
		if(template.getFieldMapping().containsKey(fieldname) && null != template.getFlagTemplate() && template.getFlagTemplate().contains(flag)) {
			flagMapping.put(dto, new FieldFlag(fieldname,flag));
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
