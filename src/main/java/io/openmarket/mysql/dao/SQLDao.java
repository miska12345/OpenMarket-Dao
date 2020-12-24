package io.openmarket.mysql.dao;

import java.util.Optional;

public interface SQLDao<T> {
    /**
     * Load an object from RDBMS using the given key.
     * @param id the primary key of the target object.
     * @return an {@link Optional} object that may contain the loaded object if key is valid, or empty otherwise.
     */
    Optional<T> load(int id);

    /**
     * Upload the given object to RDBMS.
     * @param obj the obj to upload.
     */
    void save(T obj);

    /**
     * Delete the given object from RDBMS.
     * @param obj the object to delete.
     */
    void delete(T obj);
}
