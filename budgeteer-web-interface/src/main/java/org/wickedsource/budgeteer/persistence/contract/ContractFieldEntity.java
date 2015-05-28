package org.wickedsource.budgeteer.persistence.contract;

import lombok.Data;
import org.wickedsource.budgeteer.persistence.project.ProjectContractField;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="CONTRACT_FIELD")
public class ContractFieldEntity implements Serializable{

    @Id
    @SequenceGenerator(name="SEQ_CONTRACT_FIELD_ID", sequenceName="SEQ_CONTRACT_FIELD_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONTRACT_FIELD_ID")
    private long id;

    @ManyToOne()
    @JoinColumn(name = "CONTRACT_FIELD")
    private ProjectContractField field;

    @Column(name="CONTRACT_FIELD_VALUE")
    private String value;

}
