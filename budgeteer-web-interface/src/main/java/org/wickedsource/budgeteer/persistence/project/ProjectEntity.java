package org.wickedsource.budgeteer.persistence.project;

import org.wickedsource.budgeteer.persistence.user.UserEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROJECT",
        uniqueConstraints = {
                @UniqueConstraint(name = "UNIQUE_PROJECT_NAME", columnNames = {"NAME"})
        })
public class ProjectEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PROJECT_USER",
            joinColumns = {@JoinColumn(name = "PROJECT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")})
    private List<UserEntity> authorizedUsers = new ArrayList<UserEntity>();

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

    public List<UserEntity> getAuthorizedUsers() {
        return authorizedUsers;
    }

    public void setAuthorizedUsers(List<UserEntity> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }
}
