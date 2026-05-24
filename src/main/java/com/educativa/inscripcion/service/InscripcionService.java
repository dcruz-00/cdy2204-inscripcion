package com.educativa.inscripcion.service;

import com.educativa.inscripcion.model.Curso;
import com.educativa.inscripcion.model.Inscripcion;
import com.educativa.inscripcion.repository.CursoRepository;
import com.educativa.inscripcion.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscripcionService {

    private final CursoRepository cursoRepository;
    private final InscripcionRepository inscripcionRepository;

    @Autowired
    public InscripcionService(InscripcionRepository inscripcionRepository, CursoRepository cursoRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.cursoRepository = cursoRepository;
    }

    public Inscripcion inscribir(String nombreEstudiante, List<Long> cursoIds) {
        List<Curso> cursos = (List<Curso>) cursoRepository.findAllById(cursoIds);
        double total = cursos.stream().mapToDouble(Curso::getCosto).sum();

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setNombreEstudiante(nombreEstudiante);
        inscripcion.setCursos(cursos);
        inscripcion.setTotal(total);

        return inscripcionRepository.save(inscripcion);
    }
}