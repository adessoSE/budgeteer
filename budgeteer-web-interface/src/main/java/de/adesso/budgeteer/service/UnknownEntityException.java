package de.adesso.budgeteer.service;

/**
 * Thrown when an Entity with a specified ID was not found in the database.
 */
public class UnknownEntityException extends RuntimeException {

    public UnknownEntityException(Class<?> entityClass, long id) {
        super(String.format("Entity of type %s with id %d does not exist!", entityClass, id));
    }

}
