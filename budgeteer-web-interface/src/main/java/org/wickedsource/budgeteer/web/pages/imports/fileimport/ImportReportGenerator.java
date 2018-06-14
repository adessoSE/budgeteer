package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * A class that generates the Excel-report containing the data sets that were skipped during the import process
 */
public class ImportReportGenerator {

    public static File generateReport(List<List<String>> skippedDataSets) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet worksheet = workbook.createSheet("Skipped records");

            XSSFRow row = worksheet.createRow((short) 0);
            row = worksheet.createRow((short) 1);
            XSSFCell cell = row.createCell((short) 1);
            cell.setCellValue("The following Entries were skipped during the import-process");

            worksheet.addMergedRegion(new CellRangeAddress(1,1,1,10));


            short rowIndex = 2;
            short maxColumn = 0;
            XSSFRow rowWithMaxColumn = null;
            for(List<String> entry : skippedDataSets){
                row = worksheet.createRow(rowIndex);
                short columnIndex = 0;
                for(String s : entry){
                    if(columnIndex > maxColumn){
                        rowWithMaxColumn = row;
                        maxColumn = columnIndex;
                    }
                    cell = row.createCell(columnIndex);
                    cell.setCellValue(s);
                    columnIndex++;
                }
                rowIndex++;
            }

            for(int colNum = 0; colNum<rowWithMaxColumn.getLastCellNum();colNum++)
                worksheet.autoSizeColumn(colNum);

            File temp = File.createTempFile("SkippedRecords", ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(temp);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
            return temp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
