package org.wickedsource.budgeteer.web.components.links;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;

public class ResetFilterLink extends Link {
    private WorkRecordFilter filter;
    private Page currentPage;
    private PageParameters currentPageParameters;

    public ResetFilterLink(String id, WorkRecordFilter workRecordFilter, Page page, PageParameters pageParameters) {
        super(id);
        this.filter = workRecordFilter;
        this.currentPage = page;
        this.currentPageParameters = pageParameters;
    }

    @Override
    public void onClick() {
        filter.clearFilter();
        setResponsePage(currentPage.getClass(), currentPageParameters);
    }
}
