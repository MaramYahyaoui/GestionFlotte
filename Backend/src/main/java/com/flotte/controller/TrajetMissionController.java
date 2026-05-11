package com.flotte.controller;

import com.flotte.dto.ApiResponseDTO;
import com.flotte.dto.TrajetMissionDTO;
import com.flotte.service.TrajetMissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/missions")
public class TrajetMissionController {

    @Autowired
    private TrajetMissionService missionService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<TrajetMissionDTO>>> findAll(
            @RequestParam(required = false) String statut) {
        List<TrajetMissionDTO> missions = statut != null
                ? missionService.findByStatut(statut)
                : missionService.findAll();
        return ResponseEntity.ok(ApiResponseDTO.ok(missions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Optional<TrajetMissionDTO>>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok(missionService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TrajetMissionDTO>> save(@Valid @RequestBody TrajetMissionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Mission créée", missionService.save(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TrajetMissionDTO>> update(
            @PathVariable Long id, @Valid @RequestBody TrajetMissionDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Mission mise à jour", missionService.update(id, dto)));
    }

    @PatchMapping("/{id}/terminer")
    public ResponseEntity<ApiResponseDTO<TrajetMissionDTO>> terminer(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Mission terminée", missionService.terminerMission(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        missionService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Mission supprimée", null));
    }
}
