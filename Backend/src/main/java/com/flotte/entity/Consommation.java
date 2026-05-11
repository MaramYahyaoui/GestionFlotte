package com.flotte.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consommation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consommation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "quantite_carburant", nullable = false)
    private Double quantiteCarburant;

    @Column(name = "cout_total", nullable = false)
    private Double coutTotal;
}
