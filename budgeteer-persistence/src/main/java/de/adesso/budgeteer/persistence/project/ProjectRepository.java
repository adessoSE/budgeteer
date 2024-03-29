package de.adesso.budgeteer.persistence.project;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<ProjectEntity, Long> {

  @Query(
      "SELECT (count(project) > 0) FROM ProjectEntity project WHERE project.id = :projectId AND :username IN (SELECT user.name FROM project.authorizedUsers user)")
  boolean userInAuthorizedUsers(
      @Param("username") String username, @Param("projectId") long projectId);

  @Query(
      "select pcf from ProjectContractField pcf where pcf.project.id = :projectId AND pcf.fieldName = :fieldName")
  ProjectContractField findContractFieldByName(
      @Param("projectId") long projectId, @Param("fieldName") String fieldName);

  @Query("select p from ProjectEntity p join fetch p.contractFields where p.id = :id ")
  ProjectEntity findByIdAndFetchContractFields(@Param("id") long id);

  @Query("select user.defaultProject from UserEntity user where user.id = :userId")
  Optional<ProjectEntity> getDefaultProject(@Param("userId") long userId);

  @Query("select user.authorizedProjects from UserEntity user where user.id = :userId")
  List<ProjectEntity> getUsersProjects(@Param("userId") long userId);

  @Modifying
  @Query(
      "update UserEntity user "
          + "set user.defaultProject = (select project from ProjectEntity project where project.id = :projectId) "
          + "where user.id = :userId")
  void updateDefaultProject(@Param("userId") long userId, @Param("projectId") long projectId);

  boolean existsByName(String name);

  @Query(
      "SELECT CASE WHEN COUNT(contract) > 0 THEN true ELSE false END FROM ContractEntity contract WHERE contract.project.id = :projectId")
  boolean hasContracts(@Param("projectId") long projectId);
}
