package org.wickedsource.budgeteer.persistence.budget;

import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BUDGET",
        uniqueConstraints = {
                @UniqueConstraint(name = "UNIQUE_BUDGET_NAME_PER_PROJECT", columnNames = {"name", "projectId"}),
                @UniqueConstraint(name = "UNIQUE_BUDGET_IMPORT_KEY_PER_PROJECT", columnNames = {"importKey", "projectId"})
        })
public class BudgetEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    private Money total;

    @ManyToOne(optional = false)
    @JoinColumn(name = "projectId")
    private ProjectEntity project;

    @ElementCollection
    @CollectionTable(name = "BUDGET_TAGS")
    private List<String> tags = new ArrayList<String>();

    @Column(nullable = false)
    private String importKey;

    public String getImportKey() {
        return importKey;
    }

    public void setImportKey(String importKey) {
        this.importKey = importKey;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getTotal() {
        return total;
    }

    public void setTotal(Money total) {
        this.total = total;
    }
}
