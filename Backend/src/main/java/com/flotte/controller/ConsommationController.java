package com.flotte.controller;

import com.flotte.dto.ApiResponseDTO;
import com.flotte.dto.ConsommationDTO;
import com.flotte.service.ConsommationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/consommations")
public class ConsommationController {

    @Autowired
    private ConsommationService consommationService;

    @GetMapping("/cout-total")
    public ResponseEntity<ApiResponseDTO<Double>> getCoutTotal() {
        return ResponseEntity.ok(ApiResponseDTO.ok(consommationService.getCoutTotalGlobal()));
    }
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponseDTO<List<ConsommationDTO>>> getDashboard() {
        return ResponseEntity.ok(ApiResponseDTO.ok(consommationService.findAll()));
    }

    @GetMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<ApiResponseDTO<List<ConsommationDTO>>> findByVehicule(
            @PathVariable Long vehiculeId) {
        return ResponseEntity.ok(ApiResponseDTO.ok(consommationService.findByVehicule(vehiculeId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ConsommationDTO>>> findAll() {
        return ResponseEntity.ok(ApiResponseDTO.ok(consommationService.findAll()));
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Optional<ConsommationDTO>>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(consommationService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ConsommationDTO>> save(@Valid @RequestBody ConsommationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Consommation enregistrée", consommationService.save(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ConsommationDTO>> update(
            @PathVariable Long id, @Valid @RequestBody ConsommationDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Consommation mise à jour", consommationService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        consommationService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Consommation supprimée", null));
    }
}
