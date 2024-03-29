package de.adesso.budgeteer.persistence.project;

import static org.assertj.core.api.Assertions.assertThat;

import de.adesso.budgeteer.persistence.DataJpaTestBase;
import de.adesso.budgeteer.persistence.contract.ContractEntity;
import de.adesso.budgeteer.persistence.user.UserEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProjectRepositoryTest extends DataJpaTestBase {

  @Autowired private ProjectRepository projectRepository;

  private static final Consumer<ProjectEntity> DEFAULT_PROJECT =
      projectEntity -> projectEntity.setName("project");
  private static final Consumer<UserEntity> DEFAULT_USER =
      userEntity -> {
        userEntity.setName("name");
        userEntity.setPassword("password");
      };

  @Test
  void shouldReturnTrueIfProjectWithNameExists() {
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);

    var exists = projectRepository.existsByName(project.getName());

    assertThat(exists).isTrue();
  }

  @Test
  void shouldReturnFalseIfProjectWithNameDoesNotExist() {
    persistEntity(new ProjectEntity(), DEFAULT_PROJECT);

    var exists = projectRepository.existsByName("other-project");

    assertThat(exists).isFalse();
  }

  @Test
  void shouldReturnTrueIfProjectHasContracts() {
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    persistEntity(
        new ContractEntity(),
        contractEntity -> {
          contractEntity.setName("contract");
          contractEntity.setProject(project);
          contractEntity.setTaxRate(BigDecimal.ZERO);
        });

    var hasContracts = projectRepository.hasContracts(project.getId());

    assertThat(hasContracts).isTrue();
  }

  @Test
  void shouldReturnFalseIfProjectDoesNotHaveContracts() {
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);

    var hasContracts = projectRepository.hasContracts(project.getId());

    assertThat(hasContracts).isFalse();
  }

  @Test
  void shouldReturnEmptyOptionalIfUserDoesNotHaveDefaultProject() {
    var user =
        persistEntity(
            new UserEntity(),
            DEFAULT_USER.andThen(userEntity -> userEntity.setDefaultProject(null)));

    var defaultProject = projectRepository.getDefaultProject(user.getId());

    assertThat(defaultProject).isEmpty();
  }

  @Test
  void shouldReturnDefaultProjectIfUserHasDefaultProject() {
    var project = persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    var user =
        persistEntity(
            new UserEntity(),
            DEFAULT_USER.andThen(userEntity -> userEntity.setDefaultProject(project)));

    var defaultProject = projectRepository.getDefaultProject(user.getId());

    assertThat(defaultProject).contains(project);
  }

  @Test
  void shouldReturnProjectsUserIsPartOf() {
    var user = persistEntity(new UserEntity(), DEFAULT_USER);
    var projects =
        List.of(
            persistEntity(
                new ProjectEntity(),
                DEFAULT_PROJECT.andThen(
                    projectEntity -> projectEntity.setAuthorizedUsers(List.of(user)))),
            persistEntity(
                new ProjectEntity(),
                projectEntity -> {
                  projectEntity.setName("project2");
                  projectEntity.setAuthorizedUsers(List.of(user));
                }));

    var returnedProjects = projectRepository.getUsersProjects(user.getId());

    assertThat(returnedProjects).isEqualTo(projects);
  }

  @Test
  void shouldReturnNoProjectsIfUserIsInNoProject() {
    persistEntity(new ProjectEntity(), DEFAULT_PROJECT);
    var user = persistEntity(new UserEntity(), DEFAULT_USER);

    var returnedProjects = projectRepository.getUsersProjects(user.getId());

    assertThat(returnedProjects).isEmpty();
  }

  @Test
  void shouldReturnTrueIfUserIsInAuthorizedUsers() {
    var user = persistEntity(new UserEntity(), DEFAULT_USER);
    var project =
        persistEntity(
            new ProjectEntity(),
            DEFAULT_PROJECT.andThen(
                projectEntity -> projectEntity.setAuthorizedUsers(List.of(user))));

    var returned = projectRepository.userInAuthorizedUsers(user.getName(), project.getId());

    assertThat(returned).isTrue();
  }

  @Test
  void shouldReturnFalseIfUserIsNotInAuthorizedUsers() {
    var user = persistEntity(new UserEntity(), DEFAULT_USER);
    var unauthorizedUser =
        persistEntity(
            new UserEntity(), DEFAULT_USER.andThen(userEntity -> userEntity.setName("name2")));
    var project =
        persistEntity(
            new ProjectEntity(),
            DEFAULT_PROJECT.andThen(
                projectEntity -> projectEntity.setAuthorizedUsers(List.of(user))));

    var returned =
        projectRepository.userInAuthorizedUsers(unauthorizedUser.getName(), project.getId());

    assertThat(returned).isFalse();
  }
}
