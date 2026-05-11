package com.flotte.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeDTO {

    private Long id;

    @NotBlank(message = "L'immatriculation est obligatoire")
    private String immatriculation;

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    @NotBlank(message = "Le type est obligatoire")
    private String type;

    @Min(value = 0, message = "Le kilométrage ne peut pas être négatif")
    private Integer kilometrage;

    private String statut;

    private Long   nombreMissions;
    private Double distanceTotale;
    private Double coutCarburantTotal;
}
