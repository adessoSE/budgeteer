package org.wickedsource.budgeteer.service.template;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Data
@Accessors(chain = true)
public class Template {
    private long id;
    private String name;
    private String description;
    private XSSFWorkbook wb;

    public Template(long id, String name, String description, XSSFWorkbook wb){
        this.id = id;
        this.name = name;
        this.description = description;
        this.wb = wb;
    }
}
