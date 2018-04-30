package org.wickedsource.budgeteer.web.pages.templates.templateimport;

import lombok.Data;
import org.wickedsource.budgeteer.service.template.Template;

import java.io.Serializable;
import java.util.List;

@Data
public class TemplateFormInputDto implements Serializable {
    private long projectId;
    private String name;
    private String description;
    private List<Template> files;

    public TemplateFormInputDto(long projectId) {
        this.projectId = projectId;
    }
}
