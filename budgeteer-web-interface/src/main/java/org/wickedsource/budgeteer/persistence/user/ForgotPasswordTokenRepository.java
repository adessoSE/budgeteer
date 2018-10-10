package org.wickedsource.budgeteer.persistence.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ForgotPasswordTokenRepository extends CrudRepository<ForgotPasswordToken, Long> {

    @Query("select t from ForgotPasswordToken t where t.token = :token")
    public ForgotPasswordToken findByToken(@Param("token") String token);

    @Query("select t from ForgotPasswordToken t where t.userEntity = :user")
    public ForgotPasswordToken findByUser(@Param("user") UserEntity user);
}
