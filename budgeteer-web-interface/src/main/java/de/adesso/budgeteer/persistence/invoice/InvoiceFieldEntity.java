package de.adesso.budgeteer.persistence.invoice;

import de.adesso.budgeteer.persistence.contract.ContractInvoiceField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="INVOICE_FIELD")
public class InvoiceFieldEntity implements Serializable {

    @Id
    @SequenceGenerator(name="SEQ_INVOICE_FIELD_ID", sequenceName="SEQ_INVOICE_FIELD_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INVOICE_FIELD_ID")
    private long id;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "CONTRACT_INVOICE_FIELD_ID")
    private ContractInvoiceField field;

    @Column(name="INVOICE_FIELD_VALUE", length = 255)
    private String value;


}