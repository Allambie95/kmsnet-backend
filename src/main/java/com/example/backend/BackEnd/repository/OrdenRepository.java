package com.example.backend.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.BackEnd.model.Orden;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}
