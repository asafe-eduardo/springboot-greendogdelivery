package com.boaglio.casadocodigo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boaglio.casadocodigo.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
