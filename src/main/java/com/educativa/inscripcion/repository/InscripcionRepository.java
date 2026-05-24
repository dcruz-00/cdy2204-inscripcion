package com.educativa.inscripcion.repository;

import com.educativa.inscripcion.model.Inscripcion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    
}
