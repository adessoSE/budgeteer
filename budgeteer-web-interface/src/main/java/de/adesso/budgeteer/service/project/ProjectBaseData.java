package de.adesso.budgeteer.service.project;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectBaseData implements Serializable{
    private long id;
    private String name;
}
