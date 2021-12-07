package org.wickedsource.budgeteer.persistence.project;

import lombok.Data;
import org.wickedsource.budgeteer.persistence.user.UserEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PROJECT",
        uniqueConstraints = {
                @UniqueConstraint(name = "UNIQUE_PROJECT_NAME", columnNames = {"NAME"})
        })
@Data
public class ProjectEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PROJECT_USER",
            joinColumns = {@JoinColumn(name = "PROJECT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")})
    private List<UserEntity> authorizedUsers;

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
    private List<ProjectContractField> contractFields;

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
