package org.wickedsource.budgeteer.persistence.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VerificationTokenRepository extends CrudRepository<VerificationTokenEntity, Long> {

    @Query("select t from VerificationTokenEntity t where t.token = :token")
    public VerificationTokenEntity findByToken(@Param("token") String token);

    void deleteByToken(String token);

    @Modifying
    @Query("delete from VerificationTokenEntity token where token.userEntity.id = :userId")
    void deleteByUserId(@Param("userId") long userId);

    boolean existsByToken(String token);

    @Query("select t from VerificationTokenEntity t where t.userEntity = :user")
    public VerificationTokenEntity findByUser(@Param("user") UserEntity user);
}
