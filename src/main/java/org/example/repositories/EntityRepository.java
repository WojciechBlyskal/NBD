package org.example.repositories;

import java.util.List;
import java.util.UUID;

public interface EntityRepository<T> {
    void create(T entity);
    T getById(UUID id);
    List<T> getAll();
    void update(T entity);
    void delete(T entity);
}