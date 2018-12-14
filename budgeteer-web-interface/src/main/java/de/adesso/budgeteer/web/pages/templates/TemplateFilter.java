package de.adesso.budgeteer.web.pages.templates;

import de.adesso.budgeteer.service.ReportType;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
