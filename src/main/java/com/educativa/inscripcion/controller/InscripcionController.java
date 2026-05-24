package com.educativa.inscripcion.controller;

import com.educativa.inscripcion.model.Inscripcion;
import com.educativa.inscripcion.service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @Autowired
    public InscripcionController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @PostMapping
    public Inscripcion inscribir(@RequestBody Map<String, Object> body) {
        String nombreEstudiante = (String) body.get("nombreEstudiante");
        List<Long> cursoIds = ((List<Integer>) body.get("cursoIds"))
                .stream().map(Long::valueOf).toList();
        return inscripcionService.inscribir(nombreEstudiante, cursoIds);
    }
}