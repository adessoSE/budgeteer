package org.wickedsource.budgeteer.service.contract;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.wickedsource.budgeteer.SheetTemplate.SheetTemplateSerializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicAttributeField implements Serializable, SheetTemplateSerializable{
	private String name;
	private String value;
}
