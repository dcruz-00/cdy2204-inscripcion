package com.educativa.inscripcion.repository;

import com.educativa.inscripcion.model.Curso;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    
}
