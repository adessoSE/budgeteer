package org.wickedsource.budgeteer.web.pages.templates.templateimport;

import java.io.Serializable;

import lombok.Data;

import org.wickedsource.budgeteer.service.ReportType;

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
