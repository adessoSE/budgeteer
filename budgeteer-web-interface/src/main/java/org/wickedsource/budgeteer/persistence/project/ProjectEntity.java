package org.wickedsource.budgeteer.persistence.project;

import lombok.Data;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.service.ReportType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "PROJECT",
        uniqueConstraints = {
                @UniqueConstraint(name = "UNIQUE_PROJECT_NAME", columnNames = {"NAME"})
        })
@Data
public class ProjectEntity implements Serializable {

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

    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date projectStart;

    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date projectEnd;


    /**
     * List of possible dynamic fields that can be used by contracts of this project
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project", fetch = FetchType.LAZY)
    private Set<ProjectContractField> contractFields;

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorizedUsers=" + authorizedUsers +
                ", projectStart=" + projectStart +
                ", projectEnd=" + projectEnd +
                '}';
    }
}