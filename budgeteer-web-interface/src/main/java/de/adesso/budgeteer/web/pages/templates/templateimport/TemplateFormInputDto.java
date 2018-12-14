package de.adesso.budgeteer.web.pages.templates.templateimport;

import de.adesso.budgeteer.service.ReportType;
import lombok.Data;

import java.io.Serializable;

@Data
public class TemplateFormInputDto implements Serializable {
    private long projectId;
    private String name;
    private String description;
    private ReportType type;
    private boolean isDefault;

    public TemplateFormInputDto(long projectId) {
        this.projectId = projectId;
    }
}
