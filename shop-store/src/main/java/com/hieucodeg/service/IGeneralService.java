package com.hieucodeg.service;

import java.util.List;
import java.util.Optional;

public interface IGeneralService <T> {

    List<T> findAll();

    T getById(Long id);

    Optional<T> findById(Long id);

    T save(T t);

    void remove(Long id);
}
