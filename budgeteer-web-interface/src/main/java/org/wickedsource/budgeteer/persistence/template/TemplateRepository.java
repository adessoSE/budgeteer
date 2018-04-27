package org.wickedsource.budgeteer.persistence.template;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wickedsource.budgeteer.service.template.Template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
public interface TemplateRepository extends CrudRepository<TemplateEntity, Long>{

}
*/

///Currently just a dummy repo
@Repository
public class TemplateRepository {

    private List<TemplateEntity> templates = new ArrayList<>();

    public List<TemplateEntity> findAll(){
        return templates;
    }

    public void add(List<Template> templates){
        for(Template E : templates){
            this.templates.add(new TemplateEntity(E.getName(), E.getDescription(), E.getWb()));
        }
    }
}
