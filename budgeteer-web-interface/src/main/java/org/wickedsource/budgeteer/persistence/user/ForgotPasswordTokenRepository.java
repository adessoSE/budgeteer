package org.wickedsource.budgeteer.persistence.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ForgotPasswordTokenRepository extends CrudRepository<ForgotPasswordTokenEntity, Long> {

    @Query("select t from ForgotPasswordTokenEntity t where t.token = :token")
    public ForgotPasswordTokenEntity findByToken(@Param("token") String token);

    @Query("select t from ForgotPasswordTokenEntity t where t.userEntity = :user")
    public ForgotPasswordTokenEntity findByUser(@Param("user") UserEntity user);
}
