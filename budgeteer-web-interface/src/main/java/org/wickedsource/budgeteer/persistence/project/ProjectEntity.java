package org.wickedsource.budgeteer.persistence.project;

import lombok.Data;
import org.wickedsource.budgeteer.persistence.user.UserEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "project")
    private List<ProjectContractField> contractFields;
}