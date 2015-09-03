package org.wickedsource.budgeteer.persistence.contract;

import lombok.Data;
import org.joda.money.Money;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.invoice.InvoiceEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="CONTRACT")
@Data
public class ContractEntity implements Serializable {

    public enum ContractType{
        T_UND_M,
        FIXED_PRICE
    }

    @Id
    @SequenceGenerator(name="SEQ_CONTRACT_ID", sequenceName="SEQ_CONTRACT_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONTRACT_ID")
    private long id;

    @Column(name="CONTRACT_NAME", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PROJECT_ID")
    private ProjectEntity project;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name="CONTRACT_ID")
    private List<ContractFieldEntity> contractFields = new LinkedList<ContractFieldEntity>();

    @Column(name="BUDGET")
    private Money budget;

    @Column(name="INTERNAL_NUMBER")
    private String internalNumber;

    @Column(name="START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "CONTRACT_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private ContractType type;

    @OneToMany(mappedBy="contract")
    private List<BudgetEntity> budgets = new LinkedList<BudgetEntity>();

    @Column(name = "LINK")
    private String link;

    @Lob @Basic(fetch=FetchType.LAZY)
    @Column(name = "FILE", length = 5 * 1024 * 1024) // five megabytes
    private byte[] file;

    @Column(name = "FILE_NAME")
    private String fileName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    private List<InvoiceEntity> invoices;

    /**
     * A list of possible dynamic fields that a invoice that belongs to this contract can use
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "contract", fetch = FetchType.LAZY)
    private Set<ContractInvoiceField> invoiceFields;

    @Override
    public String toString() {
        return "ContractEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", projectId=" + project.getId() +
                ", budget=" + budget +
                ", internalNumber='" + internalNumber + '\'' +
                ", type=" + type +
                ", link='" + link + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
