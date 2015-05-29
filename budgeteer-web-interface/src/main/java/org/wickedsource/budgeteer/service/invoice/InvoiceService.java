package org.wickedsource.budgeteer.service.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.persistence.contract.ContractRepository;
import org.wickedsource.budgeteer.web.pages.invoice.overview.table.InvoiceOverviewTableModel;

import javax.transaction.Transactional;

@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceDataMapper mapper;

//    @Autowired
//    private InvoiceRepository invoiceRepository;

    @Autowired
    private ContractRepository contractRepository;

    public InvoiceOverviewTableModel getInvoiceOverviewByProject(long projectId){
        InvoiceOverviewTableModel result = new InvoiceOverviewTableModel();
//        result.setInvoices(mapper.map(invoiceRepository.findByProjectId(projectId), true));
        return result;
    }

    public InvoiceOverviewTableModel getInvoiceOverviewByContract(long contractId){
        InvoiceOverviewTableModel result = new InvoiceOverviewTableModel();
//        result.setInvoices(mapper.map(invoiceRepository.findByContractId(contractId)));
        return result;
    }

//    public InvoiceBaseData getInvoiceById(long invoiceId) {
//        return mapper.map(invoiceRepository.findOne(invoiceId));
//    }

    public InvoiceBaseData getEmptyInvoiceModel(long contractId){
        ContractEntity contract = contractRepository.findOne(contractId);
        InvoiceBaseData model = new InvoiceBaseData(contractId, contract.getName());
//        List<ContractInvoiceField> fields = contract.getInvoiceFields();
//        for(ContractInvoiceField field : fields){
//            model.getInvoiceAttributes().add(new DynamicAttributeField(field.getFieldName(), ""));
//        }
        return model;
    }

    public void save(InvoiceBaseData invoiceBaseData) {
//        ContractEntity contract = contractRepository.findOne(invoiceBaseData.getContractId());
//        InvoiceEntity invoiceEntity = new InvoiceEntity();
//        invoiceEntity.setId(0);
//        invoiceEntity.setContract(contract);
//
//        if(invoiceBaseData.getInvoiceId() != 0){
//            invoiceEntity = invoiceRepository.findOne(invoiceBaseData.getInvoiceId());
//        }
//        //Update basic information
//        invoiceEntity.setName(invoiceBaseData.getInvoiceName());
//        invoiceEntity.setSum(invoiceBaseData.getSum());
//        invoiceEntity.setInternalNumber(invoiceBaseData.getInternalNumber());
//        invoiceEntity.setYear(invoiceBaseData.getYear());
//        invoiceEntity.setMonth(invoiceBaseData.getMonth());
//        invoiceEntity.setPaid(invoiceBaseData.isPaid());
//
//        //update additional information of the current contract
//        for(DynamicAttributeField fields : invoiceBaseData.getInvoiceAttributes()){
//            if(fields.getValue() != null) {
//                boolean attributeFound = false;
//                //see, if the attribute already exists -> Update the value
//                for (InvoiceFieldEntity invoiceFieldEntity : invoiceEntity.getInvoiceFields()) {
//                    if (invoiceFieldEntity.getField().getFieldName().equals(fields.getName().trim())) {
//                        invoiceFieldEntity.setValue(fields.getValue());
//                        attributeFound = true;
//                        break;
//                    }
//                }
//                // Create a new Attribute
//                if (!attributeFound) {
//                    // see if the Contract already contains a field with this name. If not, create a new one
//                    ContractInvoiceField contractInvoiceField = contractRepository.findContractFieldByName(invoiceBaseData.getContractId(), fields.getName().trim());
//                    if (contractInvoiceField == null) {
//                        contractInvoiceField = new ContractInvoiceField(0, fields.getName().trim(), contract);
//                        contract.getInvoiceFields().add(contractInvoiceField);
//                    }
//                    InvoiceFieldEntity field = new InvoiceFieldEntity();
//                    field.setId(0);
//                    field.setField(contractInvoiceField);
//                    field.setValue(fields.getValue() == null ? "" : fields.getValue().trim());
//                    invoiceEntity.getInvoiceFields().add(field);
//                }
//            }
//        }
//        invoiceRepository.save(invoiceEntity);

    }
}
