package org.wickedsource.budgeteer.service.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractFieldData implements Serializable{
    private String name;
    private String value;
}
