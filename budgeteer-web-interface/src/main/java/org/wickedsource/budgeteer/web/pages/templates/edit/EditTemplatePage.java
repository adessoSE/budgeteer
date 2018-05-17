package org.wickedsource.budgeteer.web.pages.templates.edit;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.TemplateFormInputDto;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

/**
 * Creates the form to edit a template. (Reupload, Download, Delete, Edit name/description)
 *
 * @author maximAtanasov
 */
@Mount("templates/editTemplates")
public class EditTemplatePage extends DialogPageWithBacklink {

    /**
     *
     * @param backlinkPage The page to go back to (Here, always the Template Overview page)
     * @param backlinkParameters The parameters to pass to that page.
     * @param templateID the ID of the template we want to edit.
     */
    public EditTemplatePage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters, long templateID) {
        super(backlinkPage, backlinkParameters);
        add(createBacklink("backlink1"));
        IModel<TemplateFormInputDto> formModel = model(from(new TemplateFormInputDto(BudgeteerSession.get().getProjectId())));
        this.setDefaultModel(formModel);
        Form<TemplateFormInputDto> form = new EditForm("editForm", formModel, templateID);
        add(form);
    }
}
