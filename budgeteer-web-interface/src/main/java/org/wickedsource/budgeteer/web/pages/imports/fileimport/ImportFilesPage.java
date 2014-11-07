package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.Importer;
import org.wickedsource.budgeteer.service.imports.ImportService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("import/importFiles")
public class ImportFilesPage extends DialogPageWithBacklink {

    @SpringBean
    private ImportService service;

    public ImportFilesPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        add(createBacklink("backlink1"));

        Form<ImportFormBean> form = new Form<ImportFormBean>("importForm", model(from(new ImportFormBean()))) {
            @Override
            protected void onSubmit() {
                try {
                    ImportFormBean bean = getModelObject();
                    List<InputStream> files = new ArrayList<InputStream>();
                    for (FileUpload file : bean.getFilesToImport()) {
                        files.add(file.getInputStream());
                    }
                    service.doImport(BudgeteerSession.get().getProjectId(), bean.getImporter(), files);
                    info(getString("message.success"));
                } catch (IOException e) {
                    error(getString("message.ioError"));
                } catch (ImportException e){
                    error(getString("message.importError"));
                }
            }
        };
        add(form);

        form.add(new FeedbackPanel("feedback"));
        DropDownChoice<Importer> importerChoice = new DropDownChoice<Importer>("importerChoice", model(from(form.getModel()).getImporter()), new ImportersListModel(), new ImporterChoiceRenderer());
        importerChoice.setRequired(true);
        form.add(importerChoice);
        FileUploadField fileUpload = new FileUploadField("fileUpload", model(from(form.getModel()).getFilesToImport()));
        fileUpload.setRequired(true);
        form.add(fileUpload);
        form.add(createBacklink("backlink2"));
    }

}
