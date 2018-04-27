package org.wickedsource.budgeteer.service.template;

import org.apache.wicket.model.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.imports.api.ImportException;
import org.wickedsource.budgeteer.imports.api.ImportFile;
import org.wickedsource.budgeteer.imports.api.Importer;
import org.wickedsource.budgeteer.imports.api.InvalidFileFormatException;
import org.wickedsource.budgeteer.persistence.template.TemplateEntity;
import org.wickedsource.budgeteer.persistence.template.TemplateRepository;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.TemplateFormInputDto;
import org.wickedsource.budgeteer.web.pages.templates.templateimport.TemplateImporter;

import javax.transaction.Transactional;
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
            result.add(new Template().setName(E.getName()).setDescription(E.getDescription()));
        }
        return result;
    }

    public Template getByName(String name){
        for(TemplateEntity E : templateRepository.findAll()){
            if(E.getName().equals(name)){
                return new Template().setName(E.getName()).setWb(E.getWb()).setDescription(E.getDescription());
            }
        }
        return null;
    }


    public void doImport(long projectId, TemplateImporter importer, ImportFile importFile, IModel<TemplateFormInputDto> temModel) throws InvalidFileFormatException {
        List<Template> imports = new ArrayList<>();
        try {
            imports.add(importer.read(importFile).setName(temModel.getObject().getName()).setDescription(temModel.getObject().getDescription()));
            templateRepository.add(imports);
        } catch(ImportException e){
            e.printStackTrace();
        }
    }
}
