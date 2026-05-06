package com.flotte.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotNull(message = "Le véhicule est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @NotNull(message = "Le chauffeur est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chauffeur_id", nullable = false)
    private Chauffeur chauffeur;

    @NotBlank(message = "Le point de départ est obligatoire")
    @Column(name = "point_depart", nullable = false, length = 200)
    private String pointDepart;

    @NotBlank(message = "La destination est obligatoire")
    @Column(nullable = false, length = 200)
    private String destination;

    @Positive(message = "La distance doit être positive")
    @Column(nullable = false)
    private Double distance;

    @Column(name = "date_mission")
    private LocalDate dateMission;

    @Column(length = 30)
    private String statut;
}
