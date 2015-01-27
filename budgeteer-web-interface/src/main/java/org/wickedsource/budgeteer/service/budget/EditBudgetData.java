package org.wickedsource.budgeteer.service.budget;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class EditBudgetData implements Serializable {

    private long id;
    private long projectId;
    private String title;
    private Money total;
    private String importKey;
    private List<String> tags;


    public EditBudgetData(long projectId) {
        this.projectId = projectId;
    }

    public void setTags(List<String> tags) {
        if (tags != null) {
            this.tags = new ArrayList<String>(tags);
        }
    }
}
