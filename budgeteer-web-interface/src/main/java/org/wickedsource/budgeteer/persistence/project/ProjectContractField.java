package org.wickedsource.budgeteer.persistence.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="PROJECT_CONTRACT_FIELD", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"FIELD_NAME", "PROJECT_ID"})
})
/**
 * (dynamic) Fields that could be used by a contract belonging to a project
 */
public class ProjectContractField implements Serializable{
    @Id
    @GeneratedValue()
    private long id;

    @Column(name="FIELD_NAME", nullable = false)
    private String fieldName;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private ProjectEntity project;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectContractField)) return false;

        ProjectContractField that = (ProjectContractField) o;

        if (id != that.id) return false;
        if (!fieldName.equals(that.fieldName)) return false;
        if (!(project.getId() == that.project.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + fieldName.hashCode();
        result = 31 * result + (int) project.getId();
        return result;
    }
}
