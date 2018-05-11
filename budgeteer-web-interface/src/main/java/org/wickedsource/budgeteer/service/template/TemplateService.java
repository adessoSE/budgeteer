package org.wickedsource.budgeteer.service.template;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.model.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.imports.api.ExampleFile;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.persistence.template.TemplateEntity;
import org.wickedsource.budgeteer.persistence.template.TemplateRepository;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.TemplateFormInputDto;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    /**
     *
     * @return All the templates from the repository.
     */
    public List<Template> getTemplates(){
        List<Template> result = new ArrayList<>();
        for(TemplateEntity E : templateRepository.findAll()){
            result.add(new Template(E.getId(), E.getName(), E.getDescription(), E.getType(), E.getWb(), E.getProjectId()));
        }
        return result;
    }

    /**
     * @param projectID The ID of the current project.
     * @return All the templates in the current project.
     */
    public List<Template> getTemplatesInProject(long projectID){
        List<Template> result = new ArrayList<>();
        for(TemplateEntity E : templateRepository.findByProjectId(projectID)){
            result.add(new Template(E.getId(), E.getName(), E.getDescription(), E.getType(), E.getWb(), E.getProjectId()));
        }
        return result;
    }

    /**
     * Returns a template from the database given it's ID.
     * @param templateID The ID of the template.
     * @return A new Template object.
     */
    public Template getById(long templateID){
        for(TemplateEntity E : templateRepository.findAll()){
            if(E.getId() == templateID){
                return new Template(E.getId(), E.getName(), E.getDescription(), E.getType(), E.getWb(), E.getProjectId());
            }
        }
        return null;
    }

    /**
     * Delete a template given it's id.
     * @param templateID The ID of the template.
     */
    public void deleteTemplate(long templateID){
        templateRepository.delete(templateID);
    }

    /**
     *
     * @param projectId The id of the current project
     * @param templateId The id of the template to edit
     * @param importFile The file if containing the Workbook (can be null if we do not want to reupload)
     * @param temModel The model for the template that is being edited
     * @return Returns the id of the new template, as it will be different (we create a new one with the edited data
     * and destroy the old one.
     */
    public long editTemplate(long projectId, long templateId, ImportFile importFile, IModel<TemplateFormInputDto> temModel) {
        TemplateEntity temp;
        if(importFile != null) {
            try {
                temp = new TemplateEntity(temModel.getObject().getName(), temModel.getObject().getDescription(), temModel.getObject().getType(),
                        (XSSFWorkbook)WorkbookFactory.create(importFile.getInputStream()), projectId);
                templateRepository.delete(templateId);
                templateRepository.save(temp);
                return temp.getId();
            } catch (IOException | InvalidFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }else{
            temp = new TemplateEntity(temModel.getObject().getName(),
                    temModel.getObject().getDescription(), temModel.getObject().getType(), getById(templateId).getWb(), projectId);
            templateRepository.delete(templateId);
            templateRepository.save(temp);
            return temp.getId();
        }
    }

    /**
     * Imports a template into the repository
     * @param projectId The id of the current project
     * @param importFile The file to import
     * @param temModel The data model (name, description, id).
     */
    public void doImport(long projectId, ImportFile importFile, IModel<TemplateFormInputDto> temModel) {
        List<TemplateEntity> imports = new ArrayList<>();
        try {
            imports.add(new TemplateEntity(temModel.getObject().getName(), temModel.getObject().getDescription(), temModel.getObject().getType(),
                    (XSSFWorkbook)WorkbookFactory.create(importFile.getInputStream()), projectId));
            templateRepository.save(imports);
        } catch (IOException | InvalidFormatException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @return An example file that shows how a template could look
     */
    public ExampleFile getExampleFile() {
        ExampleFile file = new ExampleFile();
        file.setFileName("example_template.xlsx");
        file.setInputStream(getClass().getResourceAsStream("/report-template.xlsx"));
        return file;
    }
}
