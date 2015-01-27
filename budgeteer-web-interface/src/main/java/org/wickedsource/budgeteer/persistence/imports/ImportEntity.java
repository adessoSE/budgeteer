package org.wickedsource.budgeteer.persistence.imports;

import lombok.Data;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "IMPORT", indexes = {@Index(name = "IMPORT_PROJECT_ID_IDX", columnList = "PROJECT_ID")})
@Data
public class ImportEntity {

    @Id
    @GeneratedValue
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date importDate;

    private Date startDate;

    private Date endDate;

    private String importType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PROJECT_ID")
    private ProjectEntity project;
}
