package de.adesso.budgeteer.rest.project.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateProjectModel {
    @NotEmpty
    private String name;
}
