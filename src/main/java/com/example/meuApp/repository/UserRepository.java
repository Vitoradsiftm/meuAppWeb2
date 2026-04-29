package com.example.meuApp.repository;




import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.meuApp.model.user;

public interface UserRepository extends JpaRepository<user, Integer> {

    Optional<user> findUserByEmail(String email);
}