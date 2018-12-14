package de.adesso.budgeteer.persistence.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long> {

    @Query("select t from VerificationToken t where t.token = :token")
    public VerificationToken findByToken(@Param("token") String token);

    @Query("select t from VerificationToken t where t.userEntity = :user")
    public VerificationToken findByUser(@Param("user") UserEntity user);
}
