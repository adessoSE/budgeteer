package org.wickedsource.budgeteer.web.pages.invoice.overview.table;

import lombok.Data;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
public class InvoiceOverviewTableModel implements Serializable{
    private List<InvoiceBaseData> invoices = new LinkedList<>();
    private List<String> footer = new LinkedList<>();

    public List<String> getHeadline() {
        List<String> result = new LinkedList<>();
        if(invoices.size() > 0){
            for(DynamicAttributeField attribute : invoices.get(0).getDynamicInvoiceFields()){
                result.add(attribute.getName());
            }
        }
        return result;
    }
}
