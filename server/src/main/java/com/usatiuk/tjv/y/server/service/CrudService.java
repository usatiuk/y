package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.EntityWithId;

import java.io.Serializable;
import java.util.Optional;

public interface CrudService<T extends EntityWithId<ID>, ID extends Serializable> {
    T create(T e);

    Optional<T> readById(ID id);

    Iterable<T> readAll();

    void update(ID id, T e);

    void deleteById(ID id);
}
