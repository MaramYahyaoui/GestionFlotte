package com.flotte.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trajet_mission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrajetMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chauffeur_id", nullable = false)
    private Chauffeur chauffeur;

    @Column(name = "point_depart", nullable = false, length = 200)
    private String pointDepart;

    @Column(nullable = false, length = 200)
    private String destination;

    @Column(nullable = false)
    private Double distance;

    @Column(name = "date_mission")
    private LocalDate dateMission;

    @Column(length = 30)
    private String statut;
}
