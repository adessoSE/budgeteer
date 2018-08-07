package org.wickedsource.budgeteer.web.pages.budgets.notes.form;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.budget.EditBudgetData;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import wicket.contrib.tinymce4.TinyMceBehavior;
import wicket.contrib.tinymce4.settings.TinyMCESettings;
import wicket.contrib.tinymce4.settings.Toolbar;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class BudgetNotesForm extends Form<EditBudgetData> {

    @SpringBean
    private BudgetService service;

    public BudgetNotesForm(String id, IModel<EditBudgetData> model) {
        super(id, model);
        Injector.get().inject(this);
        addComponents();
    }

    /**
     * Add all components to the view.
     * The editor with settings and a CustomFeedbackPanel are added.
     */
    private void addComponents() {
        TinyMCESettings settings = new TinyMCESettings(TinyMCESettings.Theme.modern);

        // The names of the plugins are stored in a string array and then added to the settings in a for-each loop.
        String plugins = "advlist autolink autosave link image lists charmap print preview hr anchor pagebreak spellchecker " +
                "searchreplace wordcount visualblocks visualchars code nonbreaking " +
                "table contextmenu directionality textcolor paste fullpage colorpicker save";
        String[] pluginNames = plugins.split(" ");
        for (String name : pluginNames)
            settings.addPlugins(name);

        // The toolbars for the editor are added to the settings.
        settings.setMenuBar(false);
        settings.addToolbar(new Toolbar("toolbar1",
                "newdocument save print spellchecker fullpage | undo redo | cut copy paste | searchreplace | formatselect fontselect fontsizeselect"));
        settings.addToolbar(new Toolbar("toolbar2",
                "forecolor backcolor | bold italic underline strikethrough | bullist numlist | alignleft aligncenter alignright alignjustify | outdent indent blockquote | link unlink anchor image code"));
        settings.addToolbar(new Toolbar("toolbar3",
                "table | hr removeformat | subscript superscript | charmap | ltr rtl | visualchars visualblocks nonbreaking pagebreak restoredraft"));

        TextArea<String> textArea = new TextArea<>("editor", model(from(getModel()).getNote()));
        textArea.add(new TinyMceBehavior(settings));
        add(textArea);

        add(new CustomFeedbackPanel("feedback"));
    }

    /**
     * When the user presses the save button or CTRL+S, the note is saved and a feedback is displayed.
     */
    @Override
    protected void onSubmit() {
        try {
            service.saveBudget(getModelObject());
            this.success("Changes successfully saved.");
        } catch (DataIntegrityViolationException e) {
            this.error("Changes could not be saved.");
        }
    }
}
