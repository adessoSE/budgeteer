package de.adesso.budgeteer.web.pages.templates;

import de.adesso.budgeteer.web.BudgeteerSession;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.templates.table.TemplateListModel;
import de.adesso.budgeteer.web.pages.templates.table.TemplatesTable;
import de.adesso.budgeteer.web.pages.templates.templateimport.ImportTemplatesPage;
import org.apache.wicket.markup.html.link.Link;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("templates")
public class TemplatesPage extends BasePage {

    public TemplatesPage() {
        TemplatesTable table = new TemplatesTable("templateTable", new TemplateListModel(BudgeteerSession.get().getTemplateFilter()));
        add(table);
        add(new TemplateFilterPanel("filter", BudgeteerSession.get().getTemplateFilter()));
        add(createImportLink("importLink"));
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, TemplatesPage.class);
    }

    private Link createImportLink(String id) {
        final ImportTemplatesPage importPage = new ImportTemplatesPage(TemplatesPage.class, getPageParameters());
        return new Link(id) {
            @Override
            public void onClick() {
                setResponsePage(importPage);
            }
        };
    }
}
