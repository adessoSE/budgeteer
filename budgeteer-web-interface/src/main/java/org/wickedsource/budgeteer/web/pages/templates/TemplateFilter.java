package org.wickedsource.budgeteer.web.pages.templates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

import org.wickedsource.budgeteer.service.ReportType;

public class TemplateFilter implements Serializable {

	@Getter
	private long projectId;

	@Getter
	private List<ReportType> typesList;

	@Getter
	private List<ReportType> possibleTypes = new LinkedList<>();

	public TemplateFilter(long projectId) {
		this.projectId = projectId;
		typesList = new ArrayList<>(Arrays.asList(ReportType.values()));
	}
}
