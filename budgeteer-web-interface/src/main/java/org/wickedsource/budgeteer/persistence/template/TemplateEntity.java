package org.wickedsource.budgeteer.persistence.template;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.type.StandardBasicTypes;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.service.template.Template;

import javax.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;


@Entity
@Table(name="TEMPLATE")
@Data
@NoArgsConstructor
public class TemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name="PROJECT_ID")
    private long projectId;

    @Column(name="NAME", length = 64)
    private String name = "";

    @Column(name="DESCRIPTION", length = 64)
    private String description = "";

    @Transient
    private XSSFWorkbook wbXSSF = new XSSFWorkbook();

    @Column(name="TEMPLATE")
    @Lob
    private byte[] wbArr;

    public TemplateEntity( String name, String description, XSSFWorkbook workbook, long projectID){
        this.name = name;
        this.description = description;
        this.wbXSSF = workbook;
        this.projectId = projectID;
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wbXSSF.write(out);
            wbArr = out.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Template getTemplate(){
        return new Template(id, name, description, wbXSSF, projectId);
    }

    public XSSFWorkbook getWb(){
        if(wbArr != null) {
            try {
                return (XSSFWorkbook) WorkbookFactory.create(new ByteArrayInputStream(wbArr));
            } catch (IOException | InvalidFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
