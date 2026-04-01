package com.example.backend.BackEnd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.BackEnd.model.NoticiaHome;
import com.example.backend.BackEnd.service.NoticiaHomeService;

@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://kmsnet.cl",
    "https://www.kmsnet.cl"
})
public class NoticiaHomeController {

    @Autowired
    private NoticiaHomeService service;

    // Público: el home lo consume
    @GetMapping("/noticia")
    public ResponseEntity<NoticiaHome> getNoticia() {
        return ResponseEntity.ok(service.getOrCreate());
    }

    // Admin: actualiza título, párrafo, imagenUrl
    @PutMapping("/noticia")
    public ResponseEntity<?> updateNoticia(@RequestBody NoticiaHome body) {
        try {
            NoticiaHome updated = service.update(body);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error actualizando noticia.");
        }
    }
}
