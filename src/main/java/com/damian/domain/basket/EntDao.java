package com.damian.domain.basket;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EntDao extends CrudRepository<Ent,Long> {

    public List<Ent> findAllBy();

}
