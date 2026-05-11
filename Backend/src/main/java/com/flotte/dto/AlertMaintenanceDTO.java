package com.flotte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data 
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Alerte de maintenance préventive pour un véhicule")
public class AlertMaintenanceDTO {
    private Long   vehiculeId;
    private String immatriculation;
    private String modele;
    private Integer kilometrageActuel;
    private Integer seuilMaintenance;
    private Integer depassement;
    @Schema(description = "Niveau d'alerte : INFO, ATTENTION, CRITIQUE")
    private String niveauAlerte;
}
