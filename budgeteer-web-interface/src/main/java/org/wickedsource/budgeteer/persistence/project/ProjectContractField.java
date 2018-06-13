package org.wickedsource.budgeteer.persistence.project;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
	name = "PROJECT_CONTRACT_FIELD",
	uniqueConstraints = {@UniqueConstraint(columnNames = {"FIELD_NAME", "PROJECT_ID"})}
)
/** (dynamic) Fields that could be used by a contract associated with a project */
public class ProjectContractField implements Serializable {
	@Id
	@SequenceGenerator(
		name = "SEQ_Project_contract_field_ID",
		sequenceName = "SEQ_Project_contract_field_ID"
	)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_Project_contract_field_ID")
	private long id;

	@Column(name = "FIELD_NAME", nullable = false)
	private String fieldName;

	@ManyToOne
	@JoinColumn(name = "PROJECT_ID", nullable = false)
	private ProjectEntity project;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProjectContractField)) return false;

		ProjectContractField that = (ProjectContractField) o;

		if (id != that.id) return false;
		if (!fieldName.equals(that.fieldName)) return false;
		if (!(project.getId() == that.project.getId())) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + fieldName.hashCode();
		result = 31 * result + (int) project.getId();
		return result;
	}

	@Override
	public String toString() {
		return "ProjectContractField{"
				+ "id="
				+ id
				+ ", fieldName='"
				+ fieldName
				+ '\''
				+ ", project="
				+ project.getId()
				+ " - "
				+ project.getName()
				+ '}';
	}
}
