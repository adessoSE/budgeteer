package org.wickedsource.budgeteer.web.components.listMultipleChoiceWithGroups;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class OptionGroup<T> implements Serializable{
    private String groupNameResourceKey;
    private List<? extends T> options;
}
