package org.wickedsource.budgeteer.persistence.user;

import org.wickedsource.budgeteer.persistence.project.ProjectEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BUDGETEER_USER",
        uniqueConstraints = {
                @UniqueConstraint(name = "UNIQUE_USER_NAME", columnNames = {"NAME"})
        })
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ProjectEntity> getAuthorizedProjects() {
        return authorizedProjects;
    }

    public void setAuthorizedProjects(List<ProjectEntity> authorizedProjects) {
        this.authorizedProjects = authorizedProjects;
    }
}
