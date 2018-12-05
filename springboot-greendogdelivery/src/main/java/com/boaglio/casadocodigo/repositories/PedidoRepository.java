package com.boaglio.casadocodigo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boaglio.casadocodigo.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

	
}
