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
public class ProjectContractField implements Serializable{
    @Id
    @GeneratedValue()
    private long id;

    @Column(name="FIELD_NAME")
    private String fieldName;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private ProjectEntity project;
}
