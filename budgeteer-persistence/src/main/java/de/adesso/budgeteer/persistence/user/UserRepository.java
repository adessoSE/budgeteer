package de.adesso.budgeteer.persistence.user;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

  @Query("select u from UserEntity u where u.name = :username and u.password = :password")
  UserEntity findByNameAndPassword(
      @Param("username") String username, @Param("password") String password);

  UserEntity findByName(String name);

  @Query(
      "select user "
          + "from UserEntity user "
          + "where user.id not in "
          + "(select userInProject.id "
          + "from UserEntity userInProject "
          + "join userInProject.authorizedProjects project "
          + "where project.id = :projectId)")
  List<UserEntity> findNotInProject(@Param("projectId") long projectId);

  @Query(
      "select user "
          + "from UserEntity user "
          + "join user.authorizedProjects project "
          + "where project.id = :projectId")
  List<UserEntity> findInProject(@Param("projectId") long projectId);

  boolean existsByName(String name);

  @Query(
      "select case when user.password = :password then true else false end "
          + "from UserEntity user "
          + "where user.id = :userId")
  boolean passwordMatches(@Param("userId") long userId, @Param("password") String password);
}
