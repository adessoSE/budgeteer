package org.wickedsource.budgeteer.SheetTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;

public class FlagTemplate {

	private static final Pattern pattern = Pattern.compile("\\{flag:(\\w{1,})\\}");
	
	Map<String,CellStyle> flagMapping;
	
	
	public FlagTemplate(Sheet sheet) {
		flagMapping = new HashMap<String,CellStyle>();
		processSheet(sheet);
	}
	
	private void processSheet(Sheet sheet) {
		for(Cell cell: sheet.getRow(0)) {
			if(containsFlag(cell)) {
				addFlag(cell);
			}
		}
	}

	private boolean containsFlag(Cell cell) {
		if(cell.getCellTypeEnum().equals(CellType.STRING)) {
			return isFlagTag(cell.getStringCellValue());
		}
		else {
			return false;
		}
	}

	public boolean contains(String flag) {
		return flagMapping.containsKey(flag);
	}
	
	public CellStyle getCellStyleFor(String flag) {
		if(!flagMapping.containsKey(flag)) {
			return null;
		}
		return flagMapping.get(flag);
	}
	
	private void addFlag(Cell cell) {
		Matcher matcher = pattern.matcher(cell.getStringCellValue());
		String flagName = null;
		if(matcher.find()) {
			 flagName = matcher.group(1);
		}
		flagMapping.put(flagName, cell.getCellStyle());
	}
	
	boolean isFlagTag(String in) {
		Matcher matcher = pattern.matcher(in);
		return matcher.matches();
	}
}
