package com.flotte.controller;

import com.flotte.dto.AlertMaintenanceDTO;
import com.flotte.dto.ApiResponseDTO;
import com.flotte.dto.VehiculeDTO;
import com.flotte.service.VehiculeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/vehicules")
public class VehiculeController {

    @Autowired
    private VehiculeService vehiculeService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<VehiculeDTO>>> findAll(
            @RequestParam(required = false) String statut) {
        List<VehiculeDTO> vehicules = statut != null
                ? vehiculeService.findByStatut(statut)
                : vehiculeService.findAll();
        return ResponseEntity.ok(ApiResponseDTO.ok(vehicules));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Optional<VehiculeDTO>>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(vehiculeService.findById(id)));
    }

    @GetMapping("/les-plus-actifs")
    public ResponseEntity<ApiResponseDTO<List<VehiculeDTO>>> getLesPlasActifs() {
        return ResponseEntity.ok(ApiResponseDTO.ok(vehiculeService.getVehiculesLesPlasActifs()));
    }

    @GetMapping("/maintenance/alertes")
    public ResponseEntity<ApiResponseDTO<List<AlertMaintenanceDTO>>> getAlertesMaintenance(
            @RequestParam(defaultValue = "10000") Integer seuil) {
        return ResponseEntity.ok(ApiResponseDTO.ok(vehiculeService.getAlertesMaintenance(seuil)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<VehiculeDTO>> save(@Valid @RequestBody VehiculeDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Véhicule créé avec succès", vehiculeService.save(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<VehiculeDTO>> update(
            @PathVariable Long id, @Valid @RequestBody VehiculeDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Véhicule mis à jour", vehiculeService.update(id, dto)));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<ApiResponseDTO<VehiculeDTO>> updateStatut(
            @PathVariable Long id, @RequestParam String statut) {
        return ResponseEntity.ok(ApiResponseDTO.ok(vehiculeService.updateStatut(id, statut)));
    }

    @PatchMapping("/{id}/kilometrage")
    public ResponseEntity<ApiResponseDTO<VehiculeDTO>> updateKilometrage(
            @PathVariable Long id, @RequestParam Integer kilometrage) {
        return ResponseEntity.ok(ApiResponseDTO.ok(vehiculeService.updateKilometrage(id, kilometrage)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        vehiculeService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Véhicule supprimé", null));
    }
}
