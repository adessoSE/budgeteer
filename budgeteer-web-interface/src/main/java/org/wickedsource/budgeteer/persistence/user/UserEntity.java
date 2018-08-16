package org.wickedsource.budgeteer.persistence.user;

import lombok.Data;
import org.hibernate.annotations.CollectionType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.MapKeyType;
import org.springframework.stereotype.Component;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.web.components.user.UserRole;

import javax.persistence.*;
import java.util.*;

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

    @Column(nullable = false, length = 1024)
    @CollectionType(type = "HashMap")
    private HashMap<Long, String[]> roles = new HashMap<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(mappedBy = "authorizedUsers")
    private List<ProjectEntity> authorizedProjects = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectEntity defaultProject;

    @Override
    public String toString(){
        return "User( Id:" + id + ", name: " +name + ")";
    }

}
