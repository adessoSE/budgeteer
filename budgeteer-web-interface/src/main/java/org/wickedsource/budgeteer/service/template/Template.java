package org.wickedsource.budgeteer.service.template;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Data
@Accessors(chain = true)
public class Template {
    private String name;
    private String description;
    private XSSFWorkbook wb;
}
