package com.flotte.entity;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "L'immatriculation est obligatoire")
    @Column(unique = true, nullable = false, length = 20)
    private String immatriculation;

    @NotBlank(message = "Le modèle est obligatoire")
    @Column(nullable = false, length = 100)
    private String modele;

    @NotBlank(message = "Le type est obligatoire")
    @Column(nullable = false, length = 50)
    private String type;

    @Min(value = 0, message = "Le kilométrage ne peut pas être négatif")
    @Column(nullable = false)
    private Integer kilometrage;

    @NotBlank(message = "Le statut est obligatoire")
    @Column(nullable = false, length = 30)
    private String statut;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrajetMission> trajets;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Consommation> consommations;

}
