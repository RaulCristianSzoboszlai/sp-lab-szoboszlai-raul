package com.example.demo.persistence;

import java.util.List;
import java.util.Optional;

/**
 * Data Access layer abstraction: the operations needed by the upper layers
 * (controllers / commands), independent of the underlying persistence technology.
 */
public interface CrudRepository<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    boolean existsById(ID id);

    void deleteById(ID id);
}
