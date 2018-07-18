package org.wickedsource.budgeteer.service.budget;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class BudgetTagFilter implements Serializable {

    private List<String> selectedTags;

    private long projectId;

    public BudgetTagFilter(List<String> selectedTags, long projectId) {
        this.selectedTags = selectedTags;
        this.projectId = projectId;
    }

    public List<String> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<String> selectedTags) {
        this.selectedTags = new ArrayList<>(selectedTags);
    }

    public void toggleTag(String tag) {
        if (selectedTags.contains(tag)) {
            selectedTags.remove(tag);
        } else {
            selectedTags.add(tag);
        }
    }

    public boolean isTagSelected(String tag) {
        return selectedTags.contains(tag);
    }

}
