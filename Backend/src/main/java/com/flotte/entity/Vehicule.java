package com.flotte.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String immatriculation;

    @Column(nullable = false, length = 100)
    private String modele;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false)
    private Integer kilometrage;

    @Column(nullable = false, length = 30)
    private String statut;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrajetMission> trajets;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consommation> consommations;
}
