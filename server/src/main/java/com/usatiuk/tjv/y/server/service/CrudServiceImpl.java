package com.usatiuk.tjv.y.server.service;

import com.usatiuk.tjv.y.server.entity.EntityWithId;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.Optional;

public abstract class CrudServiceImpl<T extends EntityWithId<ID>, ID extends Serializable>
        implements CrudService<T, ID> {
    @Override
    public T create(T e) {
        if (e == null || (e.getId() != null && getRepository().existsById(e.getId())))
            throw new IllegalArgumentException();

        return getRepository().save(e);
    }

    @Override
    public Optional<T> readById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public Iterable<T> readAll() {
        return getRepository().findAll();
    }

    @Override
    public void update(ID id, T e) {

    }

    @Override
    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    protected abstract CrudRepository<T, ID> getRepository();
}
