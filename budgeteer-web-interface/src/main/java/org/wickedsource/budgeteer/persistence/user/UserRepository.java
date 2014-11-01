package org.wickedsource.budgeteer.persistence.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<UserEntity, Long>{

    @Query("select u from UserEntity u where u.name = :name and u.password = :password")
    public UserEntity findByNameAndPassword(@Param("name") String name, @Param("password") String password);

}
