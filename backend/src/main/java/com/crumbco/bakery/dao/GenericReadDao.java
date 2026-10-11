package com.crumbco.bakery.dao;

import java.util.List;
import java.util.Optional;

/** Generic DAO contract demonstrates type-safe collections and reusable database access design. */
public interface GenericReadDao<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
}
