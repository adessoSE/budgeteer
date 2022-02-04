package de.adesso.budgeteer.persistence.contract;

import de.adesso.budgeteer.persistence.project.ProjectContractField;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "CONTRACT_FIELD")
public class ContractFieldEntity implements Serializable {

  @Id
  @SequenceGenerator(name = "SEQ_CONTRACT_FIELD_ID", sequenceName = "SEQ_CONTRACT_FIELD_ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONTRACT_FIELD_ID")
  private long id;

  @ManyToOne(
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
  @JoinColumn(name = "PROJECT_CONTRACT_FIELD")
  private ProjectContractField field;

  @Column(name = "CONTRACT_FIELD_VALUE", length = 255)
  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CONTRACT_ID")
  private ContractEntity contract;

  public ContractFieldEntity(ProjectContractField field, String value) {
    this.field = field;
    this.value = value;
  }
}
