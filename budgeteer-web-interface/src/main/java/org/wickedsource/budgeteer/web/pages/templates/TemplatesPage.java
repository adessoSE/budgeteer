package org.wickedsource.budgeteer.web.pages.templates;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.templates.table.TemplateListModel;
import org.wickedsource.budgeteer.web.pages.templates.table.TemplatesTable;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.ImportTemplatesPage;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;

@Mount("templates")
public class TemplatesPage extends BasePage {

    private WebMarkupContainer importListContainer;

    public TemplatesPage() {
        long projectId = BudgeteerSession.get().getProjectId();
        TemplatesTable table = new TemplatesTable("templateTable", new TemplateListModel(BudgeteerSession.get().getProjectId()));
        add(table);
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
    private ListView<Template> createImportsList(String id, IModel<List<Template>> model) {
        return new ListView<Template>(id, model) {
            @Override
            protected void populateItem(final ListItem<Template> item) {
                /*final Long impId = item.getModelObject().getId();
                item.add(new Label("importDate", model(from(item.getModel()).getImportDate())));
                item.add(new Label("importType", model(from(item.getModel()).getImportType())));
                item.add(new Label("numberOfFiles", model(from(item.getModel()).getNumberOfImportedFiles())));
                item.add(new Label("startDate", model(from(item.getModel()).getStartDate())));
                item.add(new Label("endDate", model(from(item.getModel()).getEndDate())));
                item.add(new AjaxLink("deleteButton") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        importService.deleteImport(impId);
                        target.add(notificationDropdown, importListContainer);
                    }
                });*/
            }
            @Override
            protected ListItem<Template> newItem(int index, IModel<Template> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<Template>(itemModel, Template.class));
            }
        };
    }
}
