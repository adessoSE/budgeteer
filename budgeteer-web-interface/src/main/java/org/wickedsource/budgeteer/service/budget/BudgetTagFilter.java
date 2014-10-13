package org.wickedsource.budgeteer.service.budget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BudgetTagFilter implements Serializable {

    public enum TagCombinationMode {
        AND,
        OR
    }

    private List<String> selectedTags = new ArrayList<String>();

    private TagCombinationMode combinationMode = TagCombinationMode.OR;

    public List<String> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<String> selectedTags) {
        this.selectedTags = new ArrayList<String>(selectedTags);
    }

    public TagCombinationMode getCombinationMode() {
        return combinationMode;
    }

    public void setCombinationMode(TagCombinationMode combinationMode) {
        this.combinationMode = combinationMode;
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
