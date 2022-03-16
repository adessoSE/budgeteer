package de.adesso.budgeteer.persistence.template;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Entity
@Table(name = "TEMPLATE")
@NoArgsConstructor
@Getter
public class TemplateEntity implements Serializable {

  @Id @GeneratedValue private long id;

  @Column(name = "PROJECT_ID")
  private long projectId;

  @Column(name = "NAME", length = 128)
  private String name;

  @Column(name = "DESCRIPTION", length = 512)
  private String description = "";

  @Column(name = "TYPE", length = 128)
  private ReportTypeEntity type;

  @Column(name = "ISDEFAULT")
  private Boolean isDefault;

  // Transient because Hibernate cannot save the Workbook directly in the db
  @Transient private transient XSSFWorkbook wbXSSF;

  // So we get the internal data and store it in a byte array.
  @Column(name = "TEMPLATE", length = 2 * 1024 * 1024)
  @Lob
  private byte[] wbArr;

  public TemplateEntity(
      String name,
      String description,
      ReportTypeEntity type,
      XSSFWorkbook workbook,
      boolean isDefault,
      long projectID) {
    this.name = name;
    this.description = description;
    this.type = type;
    this.wbXSSF = workbook;
    this.projectId = projectID;
    this.isDefault = isDefault;
    if (this.wbXSSF == null) {
      this.wbArr = null;
    } else {
      try {
        // Write the workbook to an OutputStream and then get the byteArray of it.
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wbXSSF.write(out);
        wbArr = out.toByteArray();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public TemplateEntity(
      long id,
      String name,
      String description,
      ReportTypeEntity type,
      XSSFWorkbook workbook,
      boolean isDefault,
      long projectID) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.type = type;
    this.wbXSSF = workbook;
    this.projectId = projectID;
    this.isDefault = isDefault;
    if (this.wbXSSF == null) {
      this.wbArr = null;
    } else {
      try {
        // Write the workbook to an OutputStream and then get the byteArray of it.
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wbXSSF.write(out);
        wbArr = out.toByteArray();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * We must create a new XSSFWorkbook each time we want one.
   *
   * @return A new XSSFWorkbook
   */
  public XSSFWorkbook getWb() {
    if (wbArr != null) {
      try {
        return (XSSFWorkbook) WorkbookFactory.create(new ByteArrayInputStream(wbArr));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  // This getter is necessary for old test databases that did not contain a type for the templates,
  // Causing the templates to be invisible and non-deletable by the user without a tool to browse
  // the DB.
  public ReportTypeEntity getType() {
    return Objects.requireNonNullElse(this.type, ReportTypeEntity.BUDGET_REPORT);
  }

  public boolean isDefault() {
    return Objects.requireNonNullElse(this.isDefault, false);
  }

  public enum ReportTypeEntity {
    BUDGET_REPORT("Budget"),
    CONTRACT_REPORT("Contract");

    private final String type;

    ReportTypeEntity(String type) {
      this.type = type;
    }

    @Override
    public String toString() {
      return this.type;
    }
  }
}
