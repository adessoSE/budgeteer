package org.wickedsource.budgeteer.service.template;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Template {
    private String name;
    private String description;
}
