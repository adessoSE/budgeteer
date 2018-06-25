package org.wickedsource.budgeteer.service.project;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProjectBaseData implements Serializable{
	private long id;
	private String name;
}
