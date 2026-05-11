package com.flotte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoutCarburantDTO {
    private Long   vehiculeId;
    private String immatriculation;
    private String modele;
    private Double coutTotal;
    private Double quantiteTotale;
    private Long   nombrePleins;
}
