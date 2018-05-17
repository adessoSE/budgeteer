package org.wickedsource.budgeteer.web.pages.templates.templateimport;

import lombok.Data;
import org.wickedsource.budgeteer.service.ReportType;

import java.io.Serializable;

@Data
public class TemplateFormInputDto implements Serializable {
    private long projectId;
    private String name;
    private String description;
    private ReportType type;

    public TemplateFormInputDto(long projectId) {
        this.projectId = projectId;
    }
}
