package com.example.meuApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.meuApp.model.carro;

@Repository
public interface ProductRepository extends JpaRepository<carro, Long> {

}