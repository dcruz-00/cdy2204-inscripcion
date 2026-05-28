package com.educativa.inscripcion.controller;

import com.educativa.inscripcion.model.Curso;
import com.educativa.inscripcion.model.Inscripcion;
import com.educativa.inscripcion.repository.S3Repository;
import com.educativa.inscripcion.service.InscripcionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @Autowired
    private S3Repository s3Repository;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

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

    @GetMapping("/{id}/resumen")
    public ResponseEntity<ByteArrayResource> descargarResumen(@PathVariable Long id) {
        Inscripcion inscripcion = inscripcionService.obtenerPorId(id);

        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMEN DE INSCRIPCIÓN ===\n");
        sb.append("ID: ").append(inscripcion.getId()).append("\n");
        sb.append("Estudiante: ").append(inscripcion.getNombreEstudiante()).append("\n");
        sb.append("Cursos:\n");
        for (Curso curso : inscripcion.getCursos()) {
            sb.append("  - ").append(curso.getNombre()).append(" | $").append(curso.getCosto()).append("\n");
        }
        sb.append("Total: $").append(inscripcion.getTotal()).append("\n");

        byte[] data = sb.toString().getBytes();
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type", "text/plain")
                .header("Content-disposition", "attachment; filename=\"resumen_" + id + ".txt\"")
                .body(resource);
    }

    @PostMapping("/{id}/subir-resumen")
    public ResponseEntity<String> subirResumen(@PathVariable Long id) throws IOException {
        Inscripcion inscripcion = inscripcionService.obtenerPorId(id);

        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMEN DE INSCRIPCIÓN ===\n");
        sb.append("ID: ").append(inscripcion.getId()).append("\n");
        sb.append("Estudiante: ").append(inscripcion.getNombreEstudiante()).append("\n");
        sb.append("Cursos:\n");
        for (Curso curso : inscripcion.getCursos()) {
            sb.append("  - ").append(curso.getNombre()).append(" | $").append(curso.getCosto()).append("\n");
        }
        sb.append("Total: $").append(inscripcion.getTotal()).append("\n");

        String nombreArchivo = "resumen_" + id + ".txt";
        File archivoTemp = File.createTempFile("resumen_" + id, ".txt");
        try (FileOutputStream fos = new FileOutputStream(archivoTemp)) {
            fos.write(sb.toString().getBytes());
        }

        String resultado = s3Repository.uploadFile(bucketName, id + "/" + nombreArchivo, archivoTemp);
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}