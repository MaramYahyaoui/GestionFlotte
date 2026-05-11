package com.flotte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeActifDTO {
    private Long   vehiculeId;
    private String immatriculation;
    private String modele;
    private String type;
    private Integer kilometrage;
    private long   nombreMissions;
    private double distanceTotale;
    private double coutCarburant;
}
