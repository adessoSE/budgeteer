package org.wickedsource.budgeteer.web.pages.templates.templateimport;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.wickedsource.budgeteer.imports.api.ExampleFile;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.service.ReportType;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.AbstractChoiceRenderer;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("templates/importTemplates")
public class ImportTemplatesPage extends DialogPageWithBacklink {

    @SpringBean
    private TemplateService service;

    private List<FileUpload> fileUploads = new ArrayList<FileUpload>();

    private TemplateFormInputDto templateFormInputDto = new TemplateFormInputDto(BudgeteerSession.get().getProjectId());


    public ImportTemplatesPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        add(createBacklink("backlink1"));

        IModel formModel = model(from(templateFormInputDto));

        this.setDefaultModel(formModel);
        final Form<TemplateFormInputDto> form = new Form<TemplateFormInputDto>("importForm", new ClassAwareWrappingModel<>(new Model<>(new TemplateFormInputDto(BudgeteerSession.get().getProjectId())), TemplateFormInputDto.class)) {

            @Override
            protected void onSubmit() {
                try {
                    if(model(from(templateFormInputDto)).getObject().getName() == null){
                        error(getString("message.error.no.name"));
                    }
                    if(model(from(templateFormInputDto)).getObject().getType() == null){
                        error(getString("message.error.no.type"));
                    }
                    ImportFile file = new ImportFile(fileUploads.get(0).getClientFileName(), fileUploads.get(0).getInputStream());
                    if(model(from(templateFormInputDto)).getObject().getName() != null && model(from(templateFormInputDto)).getObject().getType() != null){
                        service.doImport(BudgeteerSession.get().getProjectId(), file, model(from(templateFormInputDto)));
                        success(getString("message.success"));
                    }
                } catch (IOException e) {
                    error(String.format(getString("message.ioError"), e.getMessage()));
                }  catch (IllegalArgumentException e) {
                    error(String.format(getString("message.importError"), e.getMessage()));
                }
            }


        };

        form.setMultiPart(true);

        add(form);

        CustomFeedbackPanel feedback = new CustomFeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);

        FileUploadField fileUpload = new FileUploadField("fileUpload", new PropertyModel<>(this, "fileUploads"));
        fileUpload.setRequired(true);

        form.add(fileUpload);
        form.add(createBacklink("backlink2"));

        form.add(createExampleFileButton("exampleFileButton"));
        form.add(new TextField<>("name", model(from(templateFormInputDto).getName())));
        form.add(new TextField<>("description", model(from(templateFormInputDto).getDescription())));
        DropDownChoice<ReportType> typeDropDown = new DropDownChoice<>("type", model(from(templateFormInputDto).getType()),
                new LoadableDetachableModel<List<ReportType>>() {
                    @Override
                    protected List<ReportType> load() {
                        return Arrays.asList(ReportType.values());
                    }
                },
                new AbstractChoiceRenderer<ReportType>() {
                    @Override
                    public Object getDisplayValue(ReportType object) {
                        return object == null ? "Unnamed" : object.toString();
                    }
                });
        typeDropDown.setNullValid(false);
        form.add(typeDropDown);
    }

    /**
     * Creates a button to download an example template.
     */
    private Link createExampleFileButton(String wicketId) {
        return new Link<Void>(wicketId) {
            @Override
            public void onClick() {
                final ExampleFile downloadFile = service.getExampleFile();
                AbstractResourceStreamWriter streamWriter = new AbstractResourceStreamWriter() {
                    @Override
                    public void write(OutputStream output) throws IOException {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        IOUtils.copy(downloadFile.getInputStream(), out);
                        output.write(out.toByteArray());
                    }
                };
                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(streamWriter, downloadFile.getFileName());
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
                HttpServletResponse response = (HttpServletResponse) getRequestCycle().getResponse().getContainerResponse();
                response.setContentType(downloadFile.getContentType());
            }
        };
    }
}


