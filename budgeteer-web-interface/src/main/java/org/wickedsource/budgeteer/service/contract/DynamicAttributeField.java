package org.wickedsource.budgeteer.service.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplateSerializable;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicAttributeField implements Serializable, SheetTemplateSerializable{
    private String name;
    private String value;
}
