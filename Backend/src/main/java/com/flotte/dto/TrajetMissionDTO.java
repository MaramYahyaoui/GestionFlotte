package com.flotte.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrajetMissionDTO {

    private Long id;

    @NotNull(message = "L'identifiant du véhicule est obligatoire")
    private Long vehiculeId;

    private String vehiculeImmatriculation;

    @NotNull(message = "L'identifiant du chauffeur est obligatoire")
    private Long chauffeurId;

    private String chauffeurNom;

    @NotBlank(message = "Le point de départ est obligatoire")
    private String pointDepart;

    @NotBlank(message = "La destination est obligatoire")
    private String destination;

    private Double distance;

    private LocalDate dateMission;

    private String statut;
}
