package dao;

import java.util.List;

/**
 * A generic DAO, or "Data Access Object" for performing CRUD (Create, Read, Update, and Delete) operations on entities
 * of a specified type. Classes implementing this interface should store entities in a persistence layer, such as a
 * database.
 * @param <T> The type of the entities this DAO works with, e.g. Product or Order.
 */
public interface DAO<T> {
    /**
     * Checks if the entity exists in the persistence layer
     * @param identifier The entities identifier, e.g. product number, order number, etc.
     * @return true if an entity with the provided identifier exists, otherwise false.
     * @throws DAOException if there was an issue.
     */
    boolean exists(int identifier) throws DAOException;

    /**
     * Gets an entity from the persistence layer using its identifier.
     * @param identifier The entities identifier, e.g. product number, order number, etc.
     * @return The entity, e.g. Product, Order, etc.
     * @throws DAOException if there was an issue, or if no such entity exists.
     */
    T get(int identifier) throws DAOException;

    /**
     * Gets a list of entities of this type stored in the persistence layer.
     * @return A list of entities, e.g. Products, Orders, etc.
     * Empty list if no entities of this type were stored.
     * @throws DAOException if there was an issue.
     */
    List<T> getAll() throws DAOException;

    /**
     * Creates a new entity in the persistence layer.
     * @param newEntity The entity to save, e.g. a new Product or Order.
     * @throws DAOException if there was an issue, such as an entity with the same identifier already existing.
     */
    void create(T newEntity) throws DAOException;

    /**
     * Modifies an entity that already exists in the persistence layer.
     * For example, by passing this a Product parameter whose productNumber is 1, that Product will replace any
     * existing Product in the persistence layer with the productNumber 1.
     * @param replacement Replacement entity.
     * @throws DAOException if there was an issue, or if there was no entity to replace.
     */
    void update(T replacement) throws DAOException;
}
