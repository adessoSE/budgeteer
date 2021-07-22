package org.wickedsource.budgeteer.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.function.Consumer;

@DataJpaTest
public abstract class DataJpaTestBase {

    @Autowired protected TestEntityManager entityManager;

    protected <T> T persistEntity(T entity, Consumer<T> setter) {
        setter.accept(entity);
        var persistedEntity = entityManager.persist(entity);
        entityManager.flush();
        return persistedEntity;
    }
}
