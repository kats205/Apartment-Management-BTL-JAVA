package org.example.apartmentmanagement.Repository;

import java.util.List;

public interface GenericDAO<T, K> {
    List<T> getAll();
    T getById(K id);
    void add(T entity);
    void update(T entity, String field, Object newValue);
    void delete(K id);
}
