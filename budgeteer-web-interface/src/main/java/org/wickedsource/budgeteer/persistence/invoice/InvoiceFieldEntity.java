package org.wickedsource.budgeteer.persistence.invoice;

import lombok.Data;
import org.wickedsource.budgeteer.persistence.contract.ContractInvoiceField;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CONTRACT_INVOICE_FIELD_ID")
    private ContractInvoiceField field;

    @Column(name="INVOICE_FIELD_VALUE")
    private String value;


}