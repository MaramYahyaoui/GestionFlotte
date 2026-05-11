package com.flotte.controller;

import com.flotte.dto.ApiResponseDTO;
import com.flotte.dto.ChauffeurDTO;
import com.flotte.service.ChauffeurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/chauffeurs")
public class ChauffeurController {

    @Autowired
    private ChauffeurService chauffeurService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ChauffeurDTO>>> findAll(
            @RequestParam(required = false) String nom) {
        List<ChauffeurDTO> chauffeurs = nom != null
                ? chauffeurService.findByNom(nom)
                : chauffeurService.findAll();
        return ResponseEntity.ok(ApiResponseDTO.ok(chauffeurs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Optional<ChauffeurDTO>>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(chauffeurService.findById(id)));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponseDTO<List<ChauffeurDTO>>> findDisponibles() {
        return ResponseEntity.ok(ApiResponseDTO.ok(chauffeurService.findDisponibles()));
    }

    @GetMapping("/les-plus-actifs")
    public ResponseEntity<ApiResponseDTO<List<ChauffeurDTO>>> findLesPlasActifs() {
        return ResponseEntity.ok(ApiResponseDTO.ok(chauffeurService.findLesPlasActifs()));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ChauffeurDTO>> save(@Valid @RequestBody ChauffeurDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Chauffeur créé avec succès", chauffeurService.save(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ChauffeurDTO>> update(
            @PathVariable Long id, @Valid @RequestBody ChauffeurDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Chauffeur mis à jour", chauffeurService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        chauffeurService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Chauffeur supprimé", null));
    }
}
