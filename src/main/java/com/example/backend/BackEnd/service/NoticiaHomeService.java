package com.example.backend.BackEnd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.BackEnd.model.NoticiaHome;
import com.example.backend.BackEnd.repository.NoticiaHomeRepository;

@Service
public class NoticiaHomeService {

    private static final Long SINGLE_ID = 1L;

    @Autowired
    private NoticiaHomeRepository repo;

    /**
     * Obtiene la noticia única. Si no existe, crea una por defecto.
     */
    public NoticiaHome getOrCreate() {
        return repo.findById(SINGLE_ID).orElseGet(() -> {
            NoticiaHome def = new NoticiaHome();
            def.setId(SINGLE_ID);
            def.setTitulo("Noticias");
            def.setParrafo("Aquí podrás publicar novedades desde el panel de administración.");
            def.setImagenUrl(""); // o una url por defecto
            return repo.save(def);
        });
    }

    /**
     * Actualiza la noticia única (id=1).
     * Valida mínimo: título y párrafo no vacíos.
     */
    public NoticiaHome update(NoticiaHome incoming) {
        if (incoming == null) {
            throw new IllegalArgumentException("Body vacío.");
        }

        String titulo = incoming.getTitulo() == null ? "" : incoming.getTitulo().trim();
        String parrafo = incoming.getParrafo() == null ? "" : incoming.getParrafo().trim();

        if (titulo.isBlank()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (parrafo.isBlank()) {
            throw new IllegalArgumentException("El párrafo no puede estar vacío.");
        }

        NoticiaHome actual = getOrCreate();
        actual.setTitulo(titulo);
        actual.setParrafo(parrafo);

        // imagenUrl puede ser vacía (por si quieres dejar sin imagen)
        String imagenUrl = incoming.getImagenUrl() == null ? "" : incoming.getImagenUrl().trim();
        actual.setImagenUrl(imagenUrl);

        return repo.save(actual);
    }
}
