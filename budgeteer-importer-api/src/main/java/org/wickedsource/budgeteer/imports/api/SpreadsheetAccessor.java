package org.wickedsource.budgeteer.imports.api;

import org.apache.poi.ss.usermodel.*;

public class SpreadsheetAccessor {

    public static boolean cellContentIsValid(Row row, int cellNumber, CellContentValidator validator) {
        if (!cellIsNull(row, cellNumber)) {
            return validator.isValid(row.getCell(cellNumber).getStringCellValue());
        }
        return false;
    }

    public static boolean cellIsNull(Row row, int cellNumber) {
        if (row != null) {
            Cell cell = row.getCell(cellNumber);
            if (cell != null) {
                String cellValue = cell.getStringCellValue();
                return cellValue == null;
            }
        }
        return true;
    }
}
