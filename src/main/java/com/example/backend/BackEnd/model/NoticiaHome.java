package com.example.backend.BackEnd.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticiaHome {

    // Queremos UNA sola fila. Usaremos siempre id = 1.
    @Id
    private Long id;

    @Column(length = 120, nullable = false)
    private String titulo;

    // Texto largo en PostgreSQL → TEXT (NO @Lob)
    @Column(columnDefinition = "TEXT", nullable = false)
    private String parrafo;

    @Column(length = 2000)
    private String imagenUrl;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
