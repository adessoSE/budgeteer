package org.wickedsource.budgeteer.persistence.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
@Table(name="INVOICE")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceEntity {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CONTRACT_ID")
    private ContractEntity contract;

    @Column(name="NAME")
    private String name;

    @Column(name ="INVOICE_SUM")
    private Money sum;

    @Column(name="INTERNAL_NUMBER")
    private String internalNumber;

    @Column(name="YEAR")
    private int year;

    @Column(name="MONTH")
    private int month;

    @Column(name = "PAID")
    private boolean paid;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name="INVOICE_ID")
    /**
     * Dynamic invoice fields
     */
    private List<InvoiceFieldEntity> dynamicFields = new LinkedList<InvoiceFieldEntity>();


}
