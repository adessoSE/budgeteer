package de.adesso.budgeteer.service.contract;

import de.adesso.budgeteer.SheetTemplate.SheetTemplateSerializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicAttributeField implements Serializable, SheetTemplateSerializable {
    private String name;
    private String value;
}
