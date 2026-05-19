package com.flotte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsommationMensuelleDTO {
    private int    annee;
    private int    mois;
    private String moisLibelle;
    private Double coutTotal;
    private Double quantiteTotale;
}
