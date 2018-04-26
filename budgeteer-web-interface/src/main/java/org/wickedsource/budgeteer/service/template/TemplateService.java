package org.wickedsource.budgeteer.service.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.template.TemplateEntity;
import org.wickedsource.budgeteer.persistence.template.TemplateRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TemplateService {

    @Autowired
    TemplateRepository templateRepository;

    public List<Template> getTemplates(){
        List<Template> result = new ArrayList<>();
        for(TemplateEntity E : templateRepository.findAll()){
            result.add(new Template().setName(E.getName()).setDescription(E.getDescription()));
        }
        return result;
    }
}
