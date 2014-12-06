package org.wickedsource.budgeteer.web.pages.imports.fileimport;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.importer.aproda.AprodaWorkRecordsImporter;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.Importer;
import org.wickedsource.budgeteer.service.imports.ImportService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mount("import/importFiles")
public class ImportFilesPage extends DialogPageWithBacklink {

    @SpringBean
    private ImportService service;

    private Importer importer = new AprodaWorkRecordsImporter();

    private List<FileUpload> fileUploads = new ArrayList<FileUpload>();

    public ImportFilesPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        add(createBacklink("backlink1"));

        Form<ImportFormBean> form = new Form<ImportFormBean>("importForm", new ClassAwareWrappingModel<ImportFormBean>(new Model<ImportFormBean>(new ImportFormBean()), ImportFormBean.class)) {
            @Override
            protected void onSubmit() {
                try {
                    List<ImportFile> files = new ArrayList<ImportFile>();
                    for (FileUpload file : fileUploads) {
                        files.add(new ImportFile(file.getClientFileName(), file.getInputStream()));
                    }
                    service.doImport(BudgeteerSession.get().getProjectId(), importer, files);
                    info(getString("message.success"));
                } catch (IOException e) {
                    error(String.format(getString("message.ioError"), e.getMessage()));
                } catch (ImportException e) {
                    error(String.format(getString("message.importError"), e.getMessage()));
                }
            }
        };
        add(form);

        form.add(new FeedbackPanel("feedback"));
        DropDownChoice<Importer> importerChoice = new DropDownChoice<Importer>("importerChoice", new PropertyModel<Importer>(this, "importer"), new ImportersListModel(), new ImporterChoiceRenderer());
        importerChoice.setRequired(true);
        form.add(importerChoice);
        FileUploadField fileUpload = new FileUploadField("fileUpload", new PropertyModel<List<FileUpload>>(this, "fileUploads"));
        fileUpload.setRequired(true);
        fileUpload.add(new AttributeModifier("accept", new AcceptedFileExtensionsModel(importer)));
        form.add(fileUpload);
        form.add(createBacklink("backlink2"));
    }

}
