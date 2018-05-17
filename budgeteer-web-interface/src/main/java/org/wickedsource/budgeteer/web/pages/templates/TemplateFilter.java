package org.wickedsource.budgeteer.web.pages.templates;

import lombok.Getter;
import lombok.Setter;
import org.wickedsource.budgeteer.service.ReportType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TemplateFilter implements Serializable {

    @Getter
    @Setter
    private long projectId;

    @Getter
    private List<ReportType> typesList = new LinkedList<>();

    @Getter
    private List<ReportType> possibleTypes = new LinkedList<>();

    public TemplateFilter(long projectId) {
        this.projectId = projectId;
        typesList = new LinkedList(Arrays.asList(ReportType.values()));
    }
}
