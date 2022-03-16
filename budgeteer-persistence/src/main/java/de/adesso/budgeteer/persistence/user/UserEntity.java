package de.adesso.budgeteer.persistence.user;

import de.adesso.budgeteer.persistence.project.ProjectEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(
    name = "BUDGETEER_USER",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UNIQUE_USER_NAME",
          columnNames = {"NAME"}),
      @UniqueConstraint(
          name = "UNIQUE_USER_MAIL",
          columnNames = {"MAIL"})
    })
@Data
public class UserEntity {

  @Id @GeneratedValue private long id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(nullable = false, length = 512)
  private String password;

  @Column(nullable = true, length = 255)
  private String mail;

  @Column(nullable = true)
  private Boolean mailVerified = false;

  @LazyCollection(LazyCollectionOption.FALSE)
  @ManyToMany(mappedBy = "authorizedUsers")
  private List<ProjectEntity> authorizedProjects = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  private ProjectEntity defaultProject;

  public UserEntity() {}

  public UserEntity(
      long id,
      String name,
      String password,
      String mail,
      boolean mailVerified,
      List<ProjectEntity> authorizedProjects,
      ProjectEntity defaultProject) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.mail = mail;
    this.mailVerified = mailVerified;
    this.authorizedProjects = authorizedProjects;
    this.defaultProject = defaultProject;
  }

  @Override
  public String toString() {
    return "User( Id:" + id + ", name: " + name + ")";
  }
}
