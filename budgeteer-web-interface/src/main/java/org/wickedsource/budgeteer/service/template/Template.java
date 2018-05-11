package org.wickedsource.budgeteer.service.template;

import lombok.Getter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Serializable;

/**
 * This class contains all the data for a Template.
 * The id attribute is not auto-generated like with TemplateEntity, it is instead passed
 * into the constructor.
 */
@Getter
public class Template implements Serializable {

    private final long id;
    private final long projectID;
    private final String name;
    private final String description;
    transient private final XSSFWorkbook wb;

    /**
     * @param id the ID for the template - this must be the same as the auto-generated id
     *           of the corresponding TemplateEntity.
     * @param name The name of the template.
     * @param description The description of the template.
     * @param wb The XSSFWorkbook (Excel template itself).
     * @param projectID The ID of the current project (Templates are specific to a project).
     */
    public Template(long id, String name, String description, XSSFWorkbook wb, long projectID){
        this.id = id;
        this.name = name;
        this.description = description;
        this.wb = wb;
        this.projectID = projectID;
    }
}
