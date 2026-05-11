package com.flotte.controller;

import com.flotte.dto.ApiResponseDTO;
import com.flotte.dto.VehiculeDTO;
import com.flotte.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/reporting")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getDashboard() {
        return ResponseEntity.ok(ApiResponseDTO.ok(reportingService.getDashboard()));
    }

    @GetMapping("/top5-vehicules")
    public ResponseEntity<ApiResponseDTO<List<VehiculeDTO>>> getTop5() {
        return ResponseEntity.ok(ApiResponseDTO.ok(reportingService.getTop5VehiculesActifs()));
    }

    @GetMapping("/flotte-active")
    public ResponseEntity<ApiResponseDTO<List<Map<String, Object>>>> getFLotteActive() {
        return ResponseEntity.ok(ApiResponseDTO.ok(reportingService.getFLotteActive()));
    }

    @GetMapping("/statuts")
    public ResponseEntity<ApiResponseDTO<Map<String, Long>>> getStatuts() {
        return ResponseEntity.ok(ApiResponseDTO.ok(reportingService.getStatuts()));
    }

    @GetMapping("/missions-par-mois")
    public ResponseEntity<ApiResponseDTO<Map<String, Long>>> getMissionsParMois(
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().getYear()}") int annee) {
        return ResponseEntity.ok(ApiResponseDTO.ok(reportingService.getMissionsParMois(annee)));
    }
}
