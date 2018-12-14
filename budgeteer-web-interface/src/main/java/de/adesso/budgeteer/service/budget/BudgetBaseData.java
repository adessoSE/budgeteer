package de.adesso.budgeteer.service.budget;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetBaseData implements Serializable {

    private long id;
    private String name;
}
