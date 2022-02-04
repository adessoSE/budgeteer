package de.adesso.budgeteer.persistence.user;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

  @Query("select u from UserEntity u where u.name = :name and u.password = :password")
  UserEntity findByNameAndPassword(@Param("name") String name, @Param("password") String password);

  @Query(
      "select u from UserEntity u where (u.name = :user or u.mail = :user) and u.password = :password")
  UserEntity findByNameOrMailAndPassword(
      @Param("user") String user, @Param("password") String password);

  UserEntity findByName(String name);

  UserEntity findByMail(String mail);

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

  boolean existsByMail(String email);

  @Query(
      "select case when user.password = :password then true else false end "
          + "from UserEntity user "
          + "where user.id = :userId")
  boolean passwordMatches(@Param("userId") long userId, @Param("password") String password);

  @Query("select user.mailVerified " + "from UserEntity user " + "where user.mail = :email")
  boolean emailVerified(@Param("email") String email);

  @Modifying
  @Query(
      "update UserEntity user "
          + "set user.password = :password "
          + "where user.id = "
          + "(select forgottenPassword.userEntity.id "
          + "from ForgotPasswordTokenEntity forgottenPassword "
          + "where forgottenPassword.token = :token)")
  void changePasswordWithForgottenPasswordToken(
      @Param("token") String token, @Param("password") String password);

  @Modifying
  @Query(
      "update UserEntity user "
          + "set user.mailVerified = true "
          + "where user.id = "
          + "(select verificationToken.userEntity.id "
          + "from VerificationTokenEntity verificationToken "
          + "where verificationToken.token = :token)")
  void verifyEmail(@Param("token") String token);
}
