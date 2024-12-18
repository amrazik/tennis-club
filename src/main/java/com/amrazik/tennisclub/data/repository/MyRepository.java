package com.amrazik.tennisclub.data.repository;

import java.util.List;

public interface MyRepository<T> {
    T create(T t);
    T findById(Long id);
    List<T> findAll();
    T update(T t);
    void delete(Long id);
}
