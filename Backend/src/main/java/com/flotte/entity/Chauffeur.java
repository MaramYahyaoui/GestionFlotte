package com.flotte.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chauffeur")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chauffeur {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le numéro de permis est obligatoire")
    @Column(name = "numero_permis", unique = true, nullable = false, length = 50)
    private String permis;

    @Min(value = 0, message = "L'expérience ne peut pas être négative")
    @Max(value = 60, message = "L'expérience ne peut pas dépasser 60 ans")
    @Column(nullable = false)
    private Integer experience;

    @OneToMany(mappedBy = "chauffeur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrajetMission> trajets;
}
