package com.flotte.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsommationDTO {

    private Long id;

    @NotNull(message = "L'identifiant du véhicule est obligatoire")
    private Long vehiculeId;

    private String vehiculeImmatriculation;

    private LocalDate date;

    @NotNull(message = "La quantité de carburant est obligatoire")
    @Min(value = 0, message = "La quantité doit être positive")
    private Double quantiteCarburant;

    @NotNull(message = "Le coût total est obligatoire")
    @Min(value = 0, message = "Le coût doit être positif")
    private Double coutTotal;
}
