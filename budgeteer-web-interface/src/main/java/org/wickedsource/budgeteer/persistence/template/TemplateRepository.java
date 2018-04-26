package org.wickedsource.budgeteer.persistence.template;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
public interface TemplateRepository extends CrudRepository<TemplateEntity, Long>{

}
*/

@Repository
public class TemplateRepository {

    private List<TemplateEntity> templates = Arrays.asList(new TemplateEntity().setName("Some template").setDescription("Some description"),
            new TemplateEntity().setName("Another template").setDescription("Another description"));

    public List<TemplateEntity> findAll(){
        return templates;
    }
}
