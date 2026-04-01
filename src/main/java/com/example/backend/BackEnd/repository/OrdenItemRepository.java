package com.example.backend.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.BackEnd.model.OrdenItem;

import java.util.List;

public interface OrdenItemRepository extends JpaRepository<OrdenItem, Long> {

    // Trae el detalle de una orden (todos sus items)
    List<OrdenItem> findByOrdenId(Long ordenId);
}
