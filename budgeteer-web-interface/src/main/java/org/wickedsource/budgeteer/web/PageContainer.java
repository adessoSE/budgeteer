package org.wickedsource.budgeteer.web;

import lombok.Data;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@Data
public class PageContainer {
    private Class pageClass;

    private PageParameters pageParameters;
}
