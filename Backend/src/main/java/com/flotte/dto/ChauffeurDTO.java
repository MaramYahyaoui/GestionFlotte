package com.flotte.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChauffeurDTO {

    private Long id;

    @NotBlank(message = "Le nom du chauffeur est obligatoire")
    private String nom;

    @NotBlank(message = "Le numéro de permis est obligatoire")
    private String permis;

    @Min(value = 0,  message = "L'expérience ne peut pas être négative")
    @Max(value = 50, message = "L'expérience ne peut pas dépasser 50 ans")
    private Integer experience;

    private Long   nombreMissions;
    private Double distanceTotale;
}
