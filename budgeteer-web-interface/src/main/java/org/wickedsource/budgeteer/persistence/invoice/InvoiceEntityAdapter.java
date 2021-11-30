package org.wickedsource.budgeteer.persistence.invoice;

import de.adesso.budgeteer.core.invoice.domain.Invoice;
import de.adesso.budgeteer.core.invoice.port.out.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.contract.ContractAdapter;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractInvoiceField;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InvoiceEntityAdapter implements
        GetInvoicesInProjectPort,
        GetInvoicesInContractPort,
        GetInvoiceByIdPort,
        DeleteInvoiceByIdPort,
        CreateInvoiceEntityPort,
        UpdateInvoiceEntityPort,
        UserHasAccessToInvoicePort{
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final ContractRepository contractRepository;
    private final ProjectRepository projectRepository;
    private final ContractAdapter contractAdapter;

    @Override
    @Transactional
    public List<Invoice> getInvoicesInProject(long projectId) {
        return invoiceMapper.mapToDomain(invoiceRepository.findByProjectId(projectId));
    }

    @Override
    @Transactional
    public List<Invoice> getInvoicesInContract(long contractId) {
        return invoiceMapper.mapToDomain(invoiceRepository.findByContractId(contractId));
    }

    @Override
    @Transactional
    public Optional<Invoice> getInvoiceById(long invoiceId) {
        return invoiceRepository.findById(invoiceId).map(invoiceMapper::mapToDomain);
    }

    @Override
    @Transactional
    public void deleteInvoiceById(long invoiceId) {
        invoiceRepository.deleteById(invoiceId);
    }

    @Override
    @Transactional
    public Invoice createInvoiceEntity(CreateInvoiceEntityCommand command) {
        var contractEntity = contractRepository.findById(command.getContractId()).orElseThrow(RuntimeException::new);
        var invoiceEntity = new InvoiceEntity();

        invoiceEntity.setContract(contractEntity);
        invoiceEntity.setName(command.getInvoiceName());
        invoiceEntity.setInvoiceSum(command.getAmountOwed());
        invoiceEntity.setInternalNumber(command.getInternalNumber());
        invoiceEntity.setYear(command.getYearMonth().getYear());
        invoiceEntity.setMonth(command.getYearMonth().getMonthValue());
        invoiceEntity.setDate(Date.valueOf(LocalDate.of(invoiceEntity.getYear(), invoiceEntity.getMonth(), 1)));
        if(command.getPaidDate() != null) invoiceEntity.setPaidDate(Date.valueOf(command.getPaidDate()));
        if(command.getDueDate() != null) invoiceEntity.setDueDate(Date.valueOf(command.getDueDate()));
        if(command.getFile() != null) {
            invoiceEntity.setFileName(command.getFile().getFileName());
            invoiceEntity.setFile(command.getFile().getFile());
        }
        invoiceEntity.setLink(command.getLink());

        addDynamicFields(command.getAttributes(), contractEntity, invoiceEntity);

        return invoiceMapper.mapToDomain(invoiceRepository.save(invoiceEntity));
    }

    @Override
    @Transactional
    public Invoice updateInvoice(UpdateInvoiceEntityCommand command) {
        var invoiceEntity = invoiceRepository.findById(command.getInvoiceId()).orElseThrow(RuntimeException::new);
        var contractEntity = contractRepository.findById(command.getContractId()).orElseThrow(RuntimeException::new);

        invoiceEntity.setContract(contractEntity);
        invoiceEntity.setName(command.getInvoiceName());
        invoiceEntity.setInvoiceSum(command.getAmountOwed());
        invoiceEntity.setInternalNumber(command.getInternalNumber());
        invoiceEntity.setYear(command.getYearMonth().getYear());
        invoiceEntity.setMonth(command.getYearMonth().getMonthValue());
        invoiceEntity.setDate(Date.valueOf(LocalDate.of(invoiceEntity.getYear(), invoiceEntity.getMonth(), 1)));
        if(command.getPaidDate() != null) invoiceEntity.setPaidDate(Date.valueOf(command.getPaidDate()));
        if(command.getDueDate() != null) invoiceEntity.setDueDate(Date.valueOf(command.getDueDate()));
        if(command.getFile() != null) {
            invoiceEntity.setFileName(command.getFile().getFileName());
            invoiceEntity.setFile(command.getFile().getFile());
        }
        invoiceEntity.setLink(command.getLink());

        // update values for fields with equal name
        command.getAttributes().forEach((fieldString, valueString) -> invoiceEntity.getDynamicFields().stream()
                .filter(field -> field.getField().getFieldName().equals(fieldString))
                .forEach(field -> field.setValue(valueString)));

        // add new (non existant) fields
        invoiceEntity.getDynamicFields().stream().
                filter(field -> command.getAttributes().containsKey(field.getField().getFieldName())).
                forEach(field -> command.getAttributes().remove(field.getField().getFieldName()));
        addDynamicFields(command.getAttributes(), contractEntity, invoiceEntity);

        return invoiceMapper.mapToDomain(invoiceRepository.save(invoiceEntity));
    }

    private void addDynamicFields(Map<String, String> attributes, ContractEntity contractEntity, InvoiceEntity invoiceEntity) {
        attributes.forEach((field, value) -> {
            ContractInvoiceField contractInvoiceField = new ContractInvoiceField();
            contractInvoiceField.setContract(contractEntity);
            contractInvoiceField.setFieldName(field);

            InvoiceFieldEntity invoiceFieldEntity = new InvoiceFieldEntity();
            invoiceFieldEntity.setField(contractInvoiceField);
            invoiceFieldEntity.setValue(value);

            invoiceEntity.getDynamicFields().add(invoiceFieldEntity);
        });
    }

    @Override
    public boolean userHasAccessToInvoice(String username, long invoiceId) {
        return contractAdapter.userHasAccessToContract(username, invoiceRepository.findById(invoiceId).map(InvoiceEntity::getContract).map(ContractEntity::getId).orElseThrow());
    }
}
