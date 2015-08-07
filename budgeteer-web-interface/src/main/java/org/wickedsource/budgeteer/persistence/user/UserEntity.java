package org.wickedsource.budgeteer.persistence.user;

import lombok.Data;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BUDGETEER_USER",
        uniqueConstraints = {
                @UniqueConstraint(name = "UNIQUE_USER_NAME", columnNames = {"NAME"})
        })
@Data
public class UserEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 512)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "authorizedUsers")
    private List<ProjectEntity> authorizedProjects = new ArrayList<ProjectEntity>();

    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectEntity defaultProject;

    @Override
    public String toString(){
        return "User( Id:" + id + ", name: " +name + ")";
    }

}
