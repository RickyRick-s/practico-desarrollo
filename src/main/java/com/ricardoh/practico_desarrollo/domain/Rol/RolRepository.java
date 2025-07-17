package com.ricardoh.practico_desarrollo.domain.Rol;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findById(Integer id);
}
