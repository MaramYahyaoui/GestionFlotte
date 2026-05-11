package com.flotte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private long   totalVehicules;
    private long   vehiculesDisponibles;
    private long   vehiculesEnMission;
    private long   vehiculesEnMaintenance;
    private long   totalChauffeurs;
    private long   missionsEnCours;
    private long   totalMissions;
    private double coutCarburantTotal;
    private double distanceTotale;
    private List<AlertMaintenanceDTO> alertesMaintenance;
}
