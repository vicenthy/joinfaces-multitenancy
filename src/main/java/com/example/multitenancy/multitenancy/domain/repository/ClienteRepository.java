package com.example.multitenancy.multitenancy.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.multitenancy.multitenancy.domain.entity.Cliente;

public interface ClienteRepository  extends JpaRepository<Cliente, Long>{

	
}
