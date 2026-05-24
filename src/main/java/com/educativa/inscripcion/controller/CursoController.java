package com.educativa.inscripcion.controller;

import com.educativa.inscripcion.model.Curso;
import com.educativa.inscripcion.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    @Autowired
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<Curso> listar() {
        return cursoService.listarCursos();
    }

    @PostMapping
    public Curso agregar(@RequestBody Curso curso) {
        return cursoService.agregarCurso(curso);
    }
}