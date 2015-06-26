package org.wickedsource.budgeteer.persistence.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="CONTRACT_INVOICE_FIELD", uniqueConstraints = {
        @UniqueConstraint(name ="unique_field_name_per_contract", columnNames = {"FIELD_NAME", "CONTRACT_ID"})
})
/**
 * (dynamic) field that a invoice belonging to a contract can have
 *  necessary to make sure all invoices of the same contract have the same dynamic fields
 */
public class ContractInvoiceField implements Serializable{
    @Id
    @GeneratedValue()
    private long id;

    @Column(name="FIELD_NAME", nullable = false)
    private String fieldName;

    @ManyToOne
    @JoinColumn(name = "CONTRACT_ID", nullable = false)
    private ContractEntity contract;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContractInvoiceField)) return false;

        ContractInvoiceField that = (ContractInvoiceField) o;

        if (id != that.id) return false;
        if (!fieldName.equals(that.fieldName)) return false;
        if (!(contract.getId() == that.contract.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + fieldName.hashCode();
        result = 31 * result + (int) contract.getId();
        return result;
    }
}
