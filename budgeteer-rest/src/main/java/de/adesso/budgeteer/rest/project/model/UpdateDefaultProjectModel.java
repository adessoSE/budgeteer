package de.adesso.budgeteer.rest.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
public class UpdateDefaultProjectModel {

    @Positive private final long newDefaultProjectId;

    public UpdateDefaultProjectModel(@JsonProperty("newDefaultProjectId") long newDefaultProjectId) {
        this.newDefaultProjectId = newDefaultProjectId;
    }
}
