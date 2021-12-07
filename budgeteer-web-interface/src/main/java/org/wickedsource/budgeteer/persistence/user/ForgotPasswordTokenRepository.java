package org.wickedsource.budgeteer.persistence.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordTokenRepository extends CrudRepository<ForgotPasswordTokenEntity, Long> {
    boolean existsByToken(String token);

    ForgotPasswordTokenEntity findByToken(String token);

    void deleteByToken(String token);

    @Query("select t from ForgotPasswordTokenEntity t where t.userEntity = :user")
    public ForgotPasswordTokenEntity findByUser(@Param("user") UserEntity user);
}
