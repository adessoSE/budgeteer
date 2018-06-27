package org.wickedsource.budgeteer.web.components.listMultipleChoiceWithGroups;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptionGroup<T> implements Serializable{
	private String groupNameResourceKey;
	private List<? extends T> options;
}
