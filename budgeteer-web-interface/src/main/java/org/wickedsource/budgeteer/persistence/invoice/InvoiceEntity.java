package org.wickedsource.budgeteer.persistence.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Entity
@Table(name="INVOICE")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceEntity {

    @Id
    @SequenceGenerator(name="SEQ_INVOICE_ID", sequenceName="SEQ_INVOICE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INVOICE_ID")
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CONTRACT_ID")
    private ContractEntity contract;

    @Column(name="NAME")
    private String name;

    @Column(name ="INVOICE_SUM")
    private Money invoiceSum;

    @Column(name="INTERNAL_NUMBER")
    private String internalNumber;

    @Column(name="YEAR")
    private int year;

    @Column(name="MONTH")
    @Min(value = 0)
    @Max(value = 11)
    private int month;

    @Column(name="SENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "PAID_DATE")
    @Temporal(TemporalType.DATE)
    private Date paidDate;

    @Column(name = "URL")
    private String link;

    @Lob @Basic(fetch=FetchType.LAZY)
    @Column(name = "INVOICE_FILE", length = 5 * 1024 * 1024) // five megabytes
    private byte[] file;

    @Column(name = "FILE_NAME")
    private String fileName;



    /**
     * Dynamic invoice fields
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name="INVOICE_ID")
    private List<InvoiceFieldEntity> dynamicFields = new LinkedList<InvoiceFieldEntity>();

    @Override
    public String toString() {
        return "InvoiceEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", invoiceSum=" + invoiceSum +
                ", internalNumber='" + internalNumber + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", date=" + date +
                ", link='" + link + '\'' +
                ", fileName='" + fileName + '\'' +
                ", contractId=" + contract.getId() +
                '}';
    }
}
