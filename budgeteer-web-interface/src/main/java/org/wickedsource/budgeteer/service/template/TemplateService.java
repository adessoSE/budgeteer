package org.wickedsource.budgeteer.service.template;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.model.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.imports.api.*;
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
    TemplateRepository templateRepository;

    /**
     *
     * @return All the templates from the repository.
     */
    public List<Template> getTemplates(){
        List<Template> result = new ArrayList<>();
        for(TemplateEntity E : templateRepository.findAll()){
            result.add(new Template(E.getId(), E.getName(), E.getDescription(), E.getWb(), E.getProjectId()));
        }
        return result;
    }

    public List<Template> getTemplatesInProject(long projectID){
        List<Template> result = new ArrayList<>();
        for(TemplateEntity E : templateRepository.findAll()){
            if(E.getProjectId() == projectID) {
                result.add(new Template(E.getId(), E.getName(), E.getDescription(), E.getWb(), E.getProjectId()));
            }
        }
        return result;
    }

    public Template getById(long templateID){
        for(TemplateEntity E : templateRepository.findAll()){
            if(E.getId() == templateID){
                return new Template(E.getId(), E.getName(), E.getDescription(), E.getWb(), E.getProjectId());
            }
        }
        return null;
    }

    public void deleteTemplate(long templateID){
        templateRepository.delete(templateID);
    }

    public void editTemplate(long projectId, long templateId, ImportFile importFile, IModel<TemplateFormInputDto> temModel) {
        TemplateEntity temp;
        if(importFile != null) {
            try {
                temp = new TemplateEntity(temModel.getObject().getName(), temModel.getObject().getDescription(),
                        new XSSFWorkbook(importFile.getInputStream()), projectId);
                templateRepository.delete(templateId);
                templateRepository.save(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            temp = new TemplateEntity(temModel.getObject().getName(),
                    temModel.getObject().getDescription(), getById(templateId).getWb(), projectId);
            templateRepository.delete(templateId);
            templateRepository.save(temp);

        }
    }

    public void doImport(long projectId, ImportFile importFile, IModel<TemplateFormInputDto> temModel) {
        List<TemplateEntity> imports = new ArrayList<>();
        try {
            imports.add(new TemplateEntity(temModel.getObject().getName(), temModel.getObject().getDescription(),
                    (XSSFWorkbook)WorkbookFactory.create(importFile.getInputStream()), projectId));
            templateRepository.save(imports);
        } catch (IOException | InvalidFormatException e){
            e.printStackTrace();
        }
    }

    public ExampleFile getExampleFile() {
        ExampleFile file = new ExampleFile();
        file.setFileName("example_template.xlsx");
        file.setInputStream(getClass().getResourceAsStream("/report-template.xlsx"));
        return file;
    }
}
